package com.wine.to.up.simple.parser.service.simple_parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import org.junit.jupiter.api.Test;

import static org.junit.Assert.*;

import java.io.IOException;

/**
 * Class for integration testing of {@link Parser}
 */
class ParserIT {
    /**
     * SimpleWine base URL
     */
    private static final String URL = "https://simplewine.ru";

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
        assertTrue(numberOfPages >= 0);
    }
}