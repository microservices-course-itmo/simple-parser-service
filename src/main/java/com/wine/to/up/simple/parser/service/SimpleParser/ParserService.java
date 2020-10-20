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
import lombok.Getter;
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
    private static UpdateProducts.UpdateProductsMessage messageToKafka;
    private static String HOME_URL;
    private static String WINE_URL;

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
        long start = System.currentTimeMillis();
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
                while (pageCounter.longValue() <= PAGES_TO_PARSE) {
                    doc = Jsoup.connect(WINE_URL + pageCounter.get()).get();
                    Elements wines = doc.getElementsByAttributeValue("class", "catalog-grid__item");
                    for (Element wine : wines) {
                        if (!wineURLs.contains(wine.getElementsByAttributeValue("class", "product-snippet__name").attr("href")))
                            wineURLs.add(wine.getElementsByAttributeValue("class", "product-snippet__name").attr("href"));
                    }
                    log.debug("Parsed {} wines from url {}", wines.size(), WINE_URL + pageCounter.getAndIncrement());
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
                String wineURL = wineURLs.poll(1, TimeUnit.SECONDS);
                if (wineURL == null) {
                    return "Wine task execution";
                }
                SimpleWine wine = Parser.parseWine(Parser.URLToDocument(URL + wineURL));
                UpdateProducts.Product newProduct = commonDbHandler.putInfoToCommonDb(wine);
                if (!products.contains(newProduct)) {
                    products.add(newProduct);
                }
                dbHandler.putInfoToDB(wine);
                log.trace("Wine: {} added to database", wineCounter.getAndIncrement());
            }
        };
        List<Callable<String>> wineTasks = Collections.nCopies(15, wineTask);
        log.debug("Total {} wines", wineURLs.size());

        try {
            List<Future<String>> future = wineParserService.invokeAll(wineTasks);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            log.error("Interrupt", e);
        }
        wineParserService.shutdown();
        log.info("End of adding information to the database.");

        if (products.size() == 0) {
            log.error("\t Z E R O\tP A R S I N G");
        } else {
            message = UpdateProducts.UpdateProductsMessage.newBuilder().addAllProducts(products).build();
            kafkaSendMessageService.sendMessage(message);
            log.info("TIME : {} seconds", TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - start));
            log.info("End of parsing, {} wines collected and sent to Kafka", products.size());
            setMessage(message);
        }
    }

    public void setMessage(UpdateProducts.UpdateProductsMessage message) {
        messageToKafka = message;
    }

    public UpdateProducts.UpdateProductsMessage getMessage() {
        return messageToKafka;
    }
}