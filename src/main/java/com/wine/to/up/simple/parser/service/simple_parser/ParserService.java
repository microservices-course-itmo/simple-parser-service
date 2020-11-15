package com.wine.to.up.simple.parser.service.simple_parser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import com.wine.to.up.commonlib.messaging.KafkaMessageSender;
import com.wine.to.up.parser.common.api.schema.ParserApi;
import com.wine.to.up.simple.parser.service.simple_parser.db_handler.WineService;
import com.wine.to.up.simple.parser.service.repository.*;
import com.wine.to.up.simple.parser.service.simple_parser.mappers.WineMapper;
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
    @Value("${parser.url}")
    private String url;
    @Value("${parser.wineurl}")
    private String wineUrl;
    private static final int NUMBER_OF_THREADS = 15;
    private ParserApi.WineParsedEvent messageToKafka;
    private final ExecutorService pagesExecutor = Executors.newSingleThreadExecutor();
    private final ExecutorService winesExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    @Autowired
    KafkaMessageSender<ParserApi.WineParsedEvent> kafkaSendMessageService;
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
    @Autowired
    private WineMapper modelMapper;
    @Autowired
    private WineToDTO wineToDTO;

    /**
     * @param someURL URL to get jsoup Document
     * @return Jsoup Document class
     */
    public static Document urlToDocument(String someURL) {
        Document wineDoc = null;
        try {
            wineDoc = Jsoup.connect(someURL).get();
            while (!(wineDoc.getElementsByClass("product-page").first().children().first().className().equals("product"))) {
                log.debug("Doing re-request...");
                wineDoc = Jsoup.connect(someURL).get();
            }

        } catch (IOException e) {
            log.error("Incorrect URL address: " + someURL);
        } catch (NullPointerException e) {
            log.error("No such element on page");
        }
        return wineDoc;
    }

    private void parser(int pagesToParse) {
        long start = System.currentTimeMillis();

        List<CompletableFuture<?>> futures = new ArrayList<>();
        ArrayBlockingQueue<String> wineURLs = new ArrayBlockingQueue<>(100_000);

        WineService dbHandler = new WineService(grapesRepository, brandsRepository, countriesRepository,
                wineGrapesRepository, wineRepository, modelMapper);
        List<ParserApi.Wine> products = new ArrayList<>();

        AtomicLong pageCounter = new AtomicLong(1);
        for (int i = 0; i < NUMBER_OF_THREADS; i++) {
            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> addWineUrls(pageCounter, wineURLs, pagesToParse), pagesExecutor);
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
                        addWineToProducts(wineURL, products, dbHandler, wineCounter);
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
        generateMessageToKafka(products);
        log.info("TIME : {} min {} seconds", TimeUnit.MILLISECONDS.toMinutes(System.currentTimeMillis() - start),
                TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()
                        - TimeUnit.MILLISECONDS.toMinutes(System.currentTimeMillis() - start) * 60000 - start));
        log.info("End of parsing, {} wines collected and sent to Kafka", products.size());

    }

    /**
     * Multithreading simplewine parser with a specified number of pages
     */
    public void startParser(int pagesToParse) {
        if (pagesToParse <= Parser.parseNumberOfPages(urlToDocument(url + "/catalog/vino/")) && (pagesToParse > 0)) {
            parser(pagesToParse);
        } else {
            log.error("Set invalid number of pages: {}", pagesToParse);
        }
    }

    /**
     * Multithreading simplewine parser with maximum number of pages
     */
    public void startParser() {
        parser(Parser.parseNumberOfPages(urlToDocument(url)));
    }

    /**
     * Getting of all products
     *
     * @return Message to Kafka
     */
    public ParserApi.WineParsedEvent getMessage() {
        return messageToKafka;
    }

    private void addWineToProducts(String wineURL, List<ParserApi.Wine> products, WineService dbHandler, AtomicInteger wineCounter) {
        SimpleWine wine = Parser.parseWine(urlToDocument(url + wineURL));
        saveWineToDB(wine, dbHandler);
        ParserApi.Wine newProduct = wineToDTO.getProtoWine(wine);
        if (!products.contains(newProduct)) {
            products.add(newProduct);
        }
        log.trace("Wine: {} added to database", wineCounter.getAndIncrement());
    }

    private void saveWineToDB(SimpleWine wine, WineService dbHandler) {
        try {
            if ((wine.getBrand() != null) && (wine.getCountry() != null)
                    && !(wine.getBrand().equals(""))) {
                Thread.sleep((long) (Math.random() * 1500));
                dbHandler.saveAllWineParsedInfo(wine);
            }
        } catch (Exception e) {
            log.error("DB error ", e);
        }
    }

    private void addWineUrls(AtomicLong pageCounter, ArrayBlockingQueue<String> wineURLs, int pagesToParse) {
        try {
            while (pageCounter.longValue() <= pagesToParse) {
                Document doc = Jsoup.connect(wineUrl + pageCounter.get()).get();
                Elements wines = doc.getElementsByClass("catalog-grid__item");
                for (Element wine : wines) {
                    if (!wineURLs.contains(wine.getElementsByClass("product-snippet__name").attr("href")))
                        wineURLs.add(wine.getElementsByClass("product-snippet__name").attr("href"));
                }
                log.debug("Parsed {} wines from url {}", wines.size(),
                        wineUrl + pageCounter.getAndIncrement());
            }
        } catch (IOException e) {
            log.error("Error while parsing page: ", e);
        }
    }

    private void generateMessageToKafka(List<ParserApi.Wine> products) {
        if (products.isEmpty()) {
            log.error("\t Z E R O\tP A R S I N G");
        } else {
            ParserApi.WineParsedEvent message;
            if (products.size() >= 1000) {
                int messageSize = (int) Math.round(products.size() / 4.0);
                for (int i = 0; i < 4; i++) {
                    if (i == 3)
                        message = ParserApi.WineParsedEvent.newBuilder()
                                .addAllWines(products.subList(i * messageSize, products.size() - 1)).build();
                    else
                        message = ParserApi.WineParsedEvent.newBuilder()
                                .addAllWines(products.subList(i * messageSize, (i + 1) * messageSize - 1)).build();
                    kafkaSendMessageService.sendMessage(message);
                }
            } else {
                message = ParserApi.WineParsedEvent.newBuilder().addAllWines(products).build();
                kafkaSendMessageService.sendMessage(message);
            }
            messageToKafka = ParserApi.WineParsedEvent.newBuilder().addAllWines(products).build();
        }
    }
}