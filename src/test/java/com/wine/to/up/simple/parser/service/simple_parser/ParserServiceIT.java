package com.wine.to.up.simple.parser.service.simple_parser;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.Assert.*;

import java.io.IOException;

/**
 * Class for testing Jsoup connection used in {@link ParserService}
 */

@SpringBootTest
class ParserServiceIT {
    /**
     * SimpleWine base URL
     */
    @Value("${parser.url}")
    private String URL;

    /**
     * Testing {@link Jsoup#connect(String)} method
     * Trying to establish connection with SimpleWine base URL
     * Response 200 means that connection succeeded
     *
     * @throws IOException Wrong input URL string value
     */
    @Test
    void testURLConnection() throws IOException {
        Connection.Response res = Jsoup.connect(URL).followRedirects(false).execute();
        assertEquals(200, res.statusCode());
    }

    @Test
    void testConnectionToPage() {
        Document wineDocument = ParserService.urlToDocument("https://simplewine.ru/catalog/product/maison_francois_martenot_chemin_des_papes_cotes_du_rhone_rose_2018_075/");
        assertTrue(wineDocument != null && wineDocument.getElementsByClass("product-page").first().children().get(1).className().equals("container"));
    }
}
