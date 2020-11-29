package com.wine.to.up.simple.parser.service.simple_parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.Assert.*;

import java.io.IOException;

/**
 * Class for integration testing of {@link Parser}
 */
@SpringBootTest
public class ParserIT {
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
    public void testParseNumberOfPagesIntegration() throws IOException {
        Document testCatalogPage = Jsoup.connect(URL + "/catalog/vino/").get();
        int numberOfPages = Parser.parseNumberOfPages(testCatalogPage);
        assertTrue(numberOfPages >= 0);
    }
}