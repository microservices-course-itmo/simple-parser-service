package com.wine.to.up.simple.parser.service.simple_parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.Assert.*;

import java.io.IOException;

/**
 * Class for integration testing of {@link Parser}
 */
@SpringBootTest
class ParserIT {
    /**
     * SimpleWine base URL
     */
    @Value("${parser.url}")
    private String URL;

    /**
     * Testing {@link Parser#parseNumberOfPages(Document)} method<br>
     * Trying to get number of pages from SimpleWine catalog page by URL<br>
     * Using {@link ParserService#urlToDocument(String)} to get Document from URL
     *
     * @throws IOException Wrong input for {@link ParserService#urlToDocument(String)}
     */
    @Test
    void testParseNumberOfPagesIntegration() throws IOException {
        Document testCatalogPage = Jsoup.connect(URL + "/catalog/vino/").get();
        int numberOfPages = Parser.parseNumberOfPages(testCatalogPage);
        Assertions.assertTrue(numberOfPages >= 0);
    }

    @Test
    void testInStock() {
        Document testWinePage = ParserService.urlToDocument("https://simplewine.ru/catalog/product/two_hands_aerope_2013_075_1/"); //wine not in stock
        Document testWinePage2 = ParserService.urlToDocument("https://simplewine.ru/catalog/product/frescobaldi_chianti_castiglioni_2019_075/"); //wine in stock
        Assertions.assertTrue(testWinePage.is(":has(.js-offer-reservation-btn)") || testWinePage.is(":has(.product-buy__not-available)"));
        Assertions.assertFalse(testWinePage2.is(":has(.js-offer-reservation-btn)") || testWinePage2.is(":has(.product-buy__not-available)"));
    }
}