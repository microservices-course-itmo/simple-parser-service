package com.wine.to.up.simple.parser.service.SimpleParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;
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
    private static final int PAGES_TO_PARSE = 130; // currently max 132, lower const value for testing purposes
    private static final String HOME_URL = URL + "/catalog/vino/";
    private static final String WINE_URL = URL + "/catalog/vino/page";

    private final ExecutorService pagesExecutor = Executors.newSingleThreadExecutor();

    private final ExecutorService winesExecutor = Executors.newFixedThreadPool(10);

    public void startParser() {
        long start = System.currentTimeMillis();

        AtomicLong pageCounter = new AtomicLong(1);
        List<CompletableFuture<?>> futures = new ArrayList<>();

        ArrayBlockingQueue<String> wineUrls = new ArrayBlockingQueue<>(100_000);

        for (int i = 0; i < Parser.parseNumberOfPages(); i++) {
            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                Document doc;
                try {
                    String pageUrl = WINE_URL + pageCounter.getAndIncrement();
                    doc = Jsoup.connect(pageUrl).get();

                    Elements wines = doc.getElementsByAttributeValue("class", "catalog-grid__item");
                    log.debug("Parsed {} wines from url {}", wines.size(), pageUrl);
                    for (Element wine : wines) {
                        wineUrls.offer(wine.getElementsByAttributeValue("class", "product-snippet__name").attr("href"));
                    }
                } catch (IOException e) {
                    log.error("Error while downloading page: ", e);
                }
            }, pagesExecutor);
            futures.add(future);
        }

        AtomicLong winesCounter = new AtomicLong(1);

        CopyOnWriteArrayList<SimpleWine> wines = new CopyOnWriteArrayList<>();
        for (int i = 0; i < 15; i++) {
            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                try {
                    while (true) {
                        String wineUrl = wineUrls.poll(10, TimeUnit.SECONDS);
                        if (wineUrl == null) {
                            return;
                        }
                        log.debug("Starting parse: {}, number {}", wineUrl, winesCounter.getAndIncrement());

                        SimpleWine simpleWine = null;
                        try {
                            simpleWine = Parser.parseWine(URL + wineUrl);
                        } catch (IOException e) {
                            log.error("Error while parsing position :", e);
                        }
                        wines.add(simpleWine);
                    }
                } catch (InterruptedException e) {
                    log.error("Interrupt ", e);
                }
            }, winesExecutor);
            futures.add(future);
        }

        futures.forEach(CompletableFuture::join);

        log.info("TIME : {} minutes", TimeUnit.MILLISECONDS.toMinutes(System.currentTimeMillis() - start));

        log.info("End of parsing, {} wines collected", wines.size());
    }
}