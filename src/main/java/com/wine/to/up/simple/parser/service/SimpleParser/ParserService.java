package com.wine.to.up.simple.parser.service.SimpleParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import com.wine.to.up.commonlib.messaging.KafkaMessageSender;
import com.wine.to.up.parser.common.api.schema.UpdateProducts;
import com.wine.to.up.simple.parser.service.repository.*;
import org.jsoup.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service

public class ParserService {

    private static String URL;
    private static final int PAGES_TO_PARSE = 3; // currently max 132, lower const value for testing purposes
    private static String HOME_URL;
    private static String WINE_URL;
    private static int wine_from_url_counter = 0;

    @Value("${parser.url}")
    public void setURLStatic(String URL_FROM_PROPERTY) {
        URL = URL_FROM_PROPERTY;
        HOME_URL = URL + "/catalog/vino/";
        WINE_URL = URL + "/catalog/vino/page";
    }

    @Autowired
    KafkaMessageSender<UpdateProducts.UpdateProductsMessage> kafkaSendMessageService;
    @Autowired
    private GrapesRepository grapesRepository;
    @Autowired
    private BrandsRepository brandsRepository;
    @Autowired
    private CountriesRepository countriesRepository;
    @Autowired
    private WineGrapesRepository wineGrapesRepository;
    @Autowired
    private WineRepository wineRepository;

    public void startParser() {
        ArrayBlockingQueue<String> wineURLs = new ArrayBlockingQueue<>(100_000);
        DbHandler dbHandler = new DbHandler(grapesRepository, brandsRepository, countriesRepository,
                wineGrapesRepository, wineRepository);
        CommonDbHandler commonDbHandler = new CommonDbHandler();
        List<UpdateProducts.Product> products = new ArrayList<>();
        UpdateProducts.UpdateProductsMessage message;

        AtomicLong pageCounter = new AtomicLong(1);
        ExecutorService executorService = Executors.newFixedThreadPool(10);

        Callable<String> callableTask = () -> {
            Document doc;
            try {
                while (pageCounter.longValue() < PAGES_TO_PARSE) {
                    doc = Jsoup.connect(WINE_URL + pageCounter.getAndIncrement()).get();
                    Elements wines = doc.getElementsByAttributeValue("class", "catalog-grid__item");
                    log.debug("Parsed {} wines from url {}", wines.size(), WINE_URL + pageCounter.getAndIncrement());
                    wine_from_url_counter += wines.size();
                    for (Element wine : wines) {
                        wineURLs.add(wine.getElementsByAttributeValue("class", "product-snippet__name").attr("href"));
                    }
                    log.debug("Total {} wines", wine_from_url_counter);
                }
            } catch (IOException e) {
                log.error("Error while parsing page: ", e);
            }

            return "URL`s task execution";
        };

        List<Callable<String>> callableTasks = Collections.nCopies(1, callableTask);

        try {
            List<Future<String>> future = executorService.invokeAll(callableTasks);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            log.error("Error while adding tasks for threads: ", e);
        }
        executorService.shutdown();

        AtomicInteger wineCounter = new AtomicInteger(0);
        ExecutorService wineParserService = Executors.newFixedThreadPool(10);
        Callable<String> wineTask = () -> {  
            while (true) {
                String wineURL = wineURLs.poll(10, TimeUnit.SECONDS);
                if (wineURL == null) {
                    return "Wine task execution";
                }
                SimpleWine wine = Parser.parseWine(Parser.URLToDocument(URL + wineURL));
                dbHandler.putInfoToDB(wine);
                log.trace("Wine: {} added to database", wineCounter);
                UpdateProducts.Product newProduct = commonDbHandler.putInfoToCommonDb(wine);
                if (!products.contains(newProduct))
                    products.add(newProduct);
            }
        };
        List<Callable<String>> wineTasks = Collections.nCopies(3, wineTask);

        try {
            List<Future<String>> future = wineParserService.invokeAll(wineTasks);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            log.error("Interrupt", e);
        }
        wineParserService.shutdown();
        log.info("End of adding information to the database.");

        message = UpdateProducts.UpdateProductsMessage.newBuilder().setShopLink(URL).addAllProducts(products).build();
        kafkaSendMessageService.sendMessage(message);
        log.info("End of parsing, {} wines collected and sent to Kafka", products.size());

    }
}