package com.wine.to.up.simple.parser.service.SimpleParser;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.*;

import static org.junit.Assert.*;

import java.io.IOException;

/**
 * Class for testing {@link ParserService}
 */
public class ParserServiceTest {
    /**
     * SimpleWine base URL
     */
    private static final String URL = "https://simplewine.ru";

    /**
     * Testing {@link Jsoup#connect(String)} method
     * Trying to establish connection with {@value #URL}
     * Response 200 means that connection succeeded
     *
     * @throws IOException Wrong input URL string value
     */
    @Test
    public void testURLConnection() throws IOException {
        Connection.Response res = Jsoup.connect(URL).followRedirects(false).execute();
        assertEquals(200, res.statusCode());
    }

    /**
     * Testing {@link ParserService#URLToDocument(String)} method
     *
     * @throws IOException Wrong input URL string value
     */
    @Test
    public void testURLtoDocument() throws IOException {
        Document doc = ParserService.URLToDocument(URL);
        assertTrue(doc.title().contains("Интернет-витрина магазина SimpleWine: продажа хорошего алкоголя в Москве и Санкт-Петербурге, цены на сайте"));
    }
}
