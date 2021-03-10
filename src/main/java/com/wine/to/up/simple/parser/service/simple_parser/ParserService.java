package com.wine.to.up.simple.parser.service.simple_parser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import com.wine.to.up.commonlib.annotations.InjectEventLogger;
import com.wine.to.up.commonlib.logging.EventLogger;
import com.wine.to.up.commonlib.messaging.KafkaMessageSender;
import com.wine.to.up.parser.common.api.schema.ParserApi;
import com.wine.to.up.simple.parser.service.components.SimpleParserMetricsCollector;
import com.wine.to.up.simple.parser.service.simple_parser.db_handler.WineService;
import com.wine.to.up.simple.parser.service.simple_parser.mappers.WineMapper;
import org.jsoup.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import static com.wine.to.up.simple.parser.service.logging.SimpleParserNotableEvents.*;

@Slf4j
@Service
public class ParserService {
    @InjectEventLogger
    public static EventLogger eventLogger;
    @Value("${parser.url}")
    private String url;
    @Value("${parser.wineurl}")
    private String wineUrl;
    @Value("${parser.sparkling_wineurl}")
    private String sparklingWineUrl;
    private static final int NUMBER_OF_THREADS = 2;
    private static final String SERVICE_NAME = "simple-parser-service";
    private static final String WINE_CITY_PATH = "/catalog/vino/?setVisitorCityId=";
    private static final String SPARKLING_WINE_CITY_PATH = "/catalog/shampanskoe_i_igristoe_vino/?setVisitorCityId=";
    private final ExecutorService pagesExecutor = Executors.newSingleThreadExecutor();
    private final ExecutorService winesExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    private final KafkaMessageSender<ParserApi.WineParsedEvent> kafkaSendMessageService;
    private final WineService wineService;
    private final WineMapper wineMapper;
    private int parsedWineCounter = 0;

    public ParserService(KafkaMessageSender<ParserApi.WineParsedEvent> kafkaSendMessageService, WineService wineService, WineMapper wineMapper) {
        this.kafkaSendMessageService = kafkaSendMessageService;
        this.wineService = wineService;
        this.wineMapper = wineMapper;
    }

    /**
     * Creating Jsoup Document from URL
     *
     * @param someURL URL to get jsoup Document
     * @return Jsoup Document class
     */
    public static Document urlToDocument(String someURL) {
        Document wineDoc = null;
        try {
            wineDoc = Jsoup.connect(someURL).maxBodySize(0).get();
            if (wineDoc.is(":has(.product-page)")) {
                int rerequestNumber = 0;
                while ((rerequestNumber < 3) && !wineDoc.getElementsByClass("product-page").first().children().first().className().equals("container")) {
                    log.debug("Doing re-request: {}", rerequestNumber);
                    wineDoc = Jsoup.connect(someURL).get();
                    rerequestNumber++;
                }
            }
        } catch (IOException e) {
            log.error("Incorrect URL address: {}", someURL);
        }
        return wineDoc;
    }

    /**
     * Parsing wines, collecting and sending to Kafka
     *
     * @param pagesToParse number pages to parse
     */
    private void parser(int pagesToParse, int sparklingPagesToParse, int city) {
        SimpleParserMetricsCollector.recordParsingStarted();
        long start = System.currentTimeMillis();

        List<CompletableFuture<?>> futures = new ArrayList<>();
        ArrayBlockingQueue<String> wineURLs = new ArrayBlockingQueue<>(100_000);
        List<ParserApi.Wine> products = new ArrayList<>();

        AtomicLong pageCounter = new AtomicLong(1);
        AtomicLong sparklingPageCounter = new AtomicLong(1);
        for (int i = 0; i < NUMBER_OF_THREADS; i++) {
            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> addWineUrls(pageCounter, sparklingPageCounter, wineURLs, pagesToParse, sparklingPagesToParse, city), pagesExecutor);
            futures.add(future);
        }

        AtomicInteger wineCounter = new AtomicInteger(1);
        for (int i = 0; i < NUMBER_OF_THREADS; i++) {
            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                try {
                    while (true) {
                        String wineURL = wineURLs.poll(10, TimeUnit.SECONDS);
                        if (wineURL == null) {
                            return;
                        }
                        addWineToProducts(wineURL, products, wineService, wineCounter);
                    }
                } catch (InterruptedException e) {
                    log.error("Interrupt ", e);
                    Thread.currentThread().interrupt();
                }
            }, winesExecutor);
            futures.add(future);
        }
        futures.forEach(CompletableFuture::join);
        log.info("End of adding information to the database.");

        if (!products.isEmpty()) {
            generateMessageToKafka(products);
        }
        if (this.parsedWineCounter == 0) {
            log.error("No wines parsed!");
        }

        log.info("TIME : {} min {} seconds", TimeUnit.MILLISECONDS.toMinutes(System.currentTimeMillis() - start),
                TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()
                        - TimeUnit.MILLISECONDS.toMinutes(System.currentTimeMillis() - start) * 60000 - start));
        SimpleParserMetricsCollector.parseProcess(System.currentTimeMillis() - start);
        log.info("End of parsing, {} wines collected and sent to Kafka", this.parsedWineCounter);
        SimpleParserMetricsCollector.recordParsingCompleted("SUCCESS");
    }

    /**
     * Multithreading simplewine parser with a specified number of pages
     */
    public void startParser(int pagesToParse, int sparklingPagesToParse, int city) {
        if (pagesToParse <= Parser.parseNumberOfPages(urlToDocument(url + WINE_CITY_PATH + city))
                && sparklingPagesToParse <= Parser.parseNumberOfPages(urlToDocument(url + SPARKLING_WINE_CITY_PATH + city)) &&
                (sparklingPagesToParse > 0 || pagesToParse > 0)) {
            parser(pagesToParse, sparklingPagesToParse, city);
        } else {
            log.error("Set invalid number of pages: {}", pagesToParse);
            SimpleParserMetricsCollector.recordParsingCompleted("FAILED");
        }
    }

    /**
     * Multithreading simplewine parser with maximum number of pages
     */
    public void startParser() {
        for (int i = 1; i <= 13; i++) {
            parser(Parser.parseNumberOfPages(urlToDocument(url + WINE_CITY_PATH + i)), Parser.parseNumberOfPages(urlToDocument(url + SPARKLING_WINE_CITY_PATH + i)), i);
        }
    }

    public void startParser(int city) {
        parser(Parser.parseNumberOfPages(urlToDocument(url + WINE_CITY_PATH + city)), Parser.parseNumberOfPages(urlToDocument(url + SPARKLING_WINE_CITY_PATH + city)), city);
    }

    /**
     * Adding wine to the Product list
     *
     * @param wineURL
     * @param products
     * @param dbHandler
     * @param wineCounter
     */
    private void addWineToProducts(String wineURL, List<ParserApi.Wine> products, WineService dbHandler, AtomicInteger wineCounter) throws InterruptedException {
        long winePageParseStart = System.currentTimeMillis();
        Document wineDocument = urlToDocument(url + wineURL);
        SimpleParserMetricsCollector.fetchDetailsWine(new Date().getTime() - winePageParseStart);
        if (wineDocument != null && wineDocument.getElementsByClass("product-page").first().children().first().className().equals("container")) {
            SimpleWine wine = Parser.parseWine(wineDocument);
            saveWineToDB(wine, dbHandler);
            ParserApi.Wine newProduct = wineMapper.toKafka(wine).build();
            if (!products.contains(newProduct)) {
                products.add(newProduct);
                if (products.size() == 5) {
                    generateMessageToKafka(products);
                }
                SimpleParserMetricsCollector.winesPublishedToKafka();
                eventLogger.info(I_WINE_DETAILS_PARSED, url + wineURL);
            }
            log.trace("Wine: {} added to database", wineCounter.getAndIncrement());
        } else {
            eventLogger.warn(W_WINE_DETAILS_PARSING_FAILED, url + wineURL);
        }
    }

    /**
     * Saving all parsed wines info to database
     *
     * @param wine
     * @param dbHandler
     */
    private void saveWineToDB(SimpleWine wine, WineService dbHandler) {
        try {
            if ((wine.getBrand() != null) && (wine.getCountry() != null)
                    && !(wine.getBrand().equals(""))) {
                Thread.sleep((long) (Math.random() * 1500));
                dbHandler.saveAllWineParsedInfo(wine);
            }
        } catch (Exception e) {
            log.error("DB error ", e);
            eventLogger.warn(W_SOME_WARN_EVENT, "{} caused by: {}", e.getMessage(), e.getCause());
        }
    }

    /**
     * Collecting all wine URLs
     *
     * @param pageCounter
     * @param wineURLs
     * @param pagesToParse
     */
    private void addWineUrls(AtomicLong pageCounter, AtomicLong sparklingPageCounter, ArrayBlockingQueue<String> wineURLs, int pagesToParse, int sparklingPagesToParse, int city) {
        parseUrls(wineUrl, pageCounter, pagesToParse, wineURLs, city);
        parseUrls(sparklingWineUrl, sparklingPageCounter, sparklingPagesToParse, wineURLs, city);
    }

    private void parseUrls(String baseURL, AtomicLong pageCounter, int pagesToParse, ArrayBlockingQueue<String> wineURLs, int city) {
        try {
            while (pageCounter.longValue() <= pagesToParse) {
                long winePageCatalogFetchStart = System.currentTimeMillis();
                Document doc = Jsoup.connect(baseURL + pageCounter.get() + "/?setVisitorCityId=" + city).maxBodySize(0).get();
                SimpleParserMetricsCollector.fetchWinePage(new Date().getTime() - winePageCatalogFetchStart);

                long winePageCatalogParseStart = System.currentTimeMillis();
                Elements wines = doc.getElementsByClass("catalog-grid__item");
                for (Element wine : wines) {
                    if (!wineURLs.contains(wine.getElementsByClass("product-snippet__name").attr("href")))
                        wineURLs.add(wine.getElementsByClass("product-snippet__name").attr("href"));
                }
                log.debug("Parsed {} wines from url {}", wines.size(),
                        baseURL + pageCounter.get());
                eventLogger.info(I_WINES_PAGE_PARSED, baseURL + pageCounter.getAndIncrement());
                SimpleParserMetricsCollector.winePageParsingDuration(new Date().getTime() - winePageCatalogParseStart);
            }
        } catch (IOException e) {
            log.error("Error while parsing page: ", e);
            eventLogger.warn(W_WINE_PAGE_PARSING_FAILED, baseURL + pageCounter.get());
        }
    }

    /**
     * @param products
     */
    private void generateMessageToKafka(List<ParserApi.Wine> products) {
        this.parsedWineCounter += products.size();
        kafkaSendMessageService.sendMessage(ParserApi.WineParsedEvent.newBuilder().
                setShopLink(url).
                setParserName(SERVICE_NAME).
                addAllWines(products).
                build());
        products.clear();
    }

    public void generateDividedMessageToKafka(List<ParserApi.Wine> products) {
        if (products.isEmpty()) {
            log.error("Database is empty");
        } else {
            if (products.size() >= 1000) {
                int messageSize = (int) Math.round(products.size() / 10.0);
                for (int i = 0; i < 10; i++) {
                    ParserApi.WineParsedEvent.Builder dividedMessage = ParserApi.WineParsedEvent
                            .newBuilder().setShopLink(url).setParserName(SERVICE_NAME);
                    if (i == 9)
                        dividedMessage.addAllWines(products.subList(i * messageSize, products.size()));
                    else
                        dividedMessage.addAllWines(products.subList(i * messageSize, (i + 1) * messageSize));
                    kafkaSendMessageService.sendMessage(dividedMessage.build());
                }
            } else {
                kafkaSendMessageService.sendMessage(ParserApi.WineParsedEvent.newBuilder()
                        .setShopLink(url).setParserName(SERVICE_NAME).addAllWines(products).build());
            }
        }
    }
}