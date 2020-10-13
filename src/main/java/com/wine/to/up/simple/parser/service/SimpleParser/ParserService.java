package com.wine.to.up.simple.parser.service.SimpleParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
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
import org.springframework.stereotype.Service;

@Slf4j
@Service
// @PropertySource(value = "application.properties")
public class ParserService {

    // @Value("${parser.url}")
    private static final String URL = "https://simplewine.ru";
    private static final int PAGES_TO_PARSE = 3; // currently max 132, lower const value for testing purposes
    private static final String HOME_URL = URL + "/catalog/vino/";
    private static final String WINE_URL = URL + "/catalog/vino/page";

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
        ArrayList<String> wineURLs = new ArrayList<>();
        DbHandler dbHandler = new DbHandler(grapesRepository, brandsRepository, countriesRepository,
                wineGrapesRepository, wineRepository);
        CommonDbHandler commonDbHandler = new CommonDbHandler();
        List<UpdateProducts.Product> products = new ArrayList<>();
        UpdateProducts.UpdateProductsMessage message;

        AtomicLong pageCounter = new AtomicLong(1);
        ExecutorService executorService = Executors.newFixedThreadPool(10);

        Callable<String> callableTask = () -> {
            Document doc;
            while (pageCounter.longValue() < PAGES_TO_PARSE) {
                doc = Jsoup.connect(WINE_URL + pageCounter.getAndIncrement()).get();
                Elements wines = doc.getElementsByAttributeValue("class", "catalog-grid__item");

                for (Element wine : wines) {
                    wineURLs.add(wine.getElementsByAttributeValue("class", "product-snippet__name").attr("href"));

                }
            }
            return "URL`s task execution";
        };

        List<Callable<String>> callableTasks = Collections.nCopies(20, callableTask);

        try {
            List<Future<String>> future = executorService.invokeAll(callableTasks);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        executorService.shutdown();

        AtomicInteger wineCounter = new AtomicInteger(0);
        ExecutorService wineParserService = Executors.newFixedThreadPool(30);
        Callable<String> wineTask = () -> {
            while (wineCounter.longValue() < wineURLs.size()) {
                SimpleWine wine = Parser.parseWine(URL + wineURLs.get(wineCounter.getAndIncrement()));
                dbHandler.putInfoToDB(wine);
                UpdateProducts.Product newProduct = commonDbHandler.putInfoToCommonDb(wine);
                if (!products.contains(newProduct))
                    products.add(newProduct);
            }

            return "Wine task execution";
        };
        List<Callable<String>> wineTasks = Collections.nCopies(30, wineTask);

        try {
            List<Future<String>> future = wineParserService.invokeAll(wineTasks);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        wineParserService.shutdown();
        log.info("\tEnd of adding information to the database.");


        message = UpdateProducts.UpdateProductsMessage.newBuilder().setShopLink(URL).addAllProducts(products).build();
        kafkaSendMessageService.sendMessage(message);
        log.info("End of parsing, {} wines collected", products.size());

    }
}