package com.wine.to.up.simple.parser.service.simple_parser;

import org.jsoup.nodes.Document;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.*;

import java.io.IOException;

/**
 * Class for testing {@link ParserService}
 */
class ParserServiceTest {
    /**
     * Testing {@link ParserService#urlToDocument(String)} method
     *
     * @throws IOException Wrong input URL string value
     */
    @Test
    void testURLtoDocument() throws IOException {
        Document doc = ParserService.urlToDocument("https://simplewine.ru/catalog/product/lindeman_s_bin_50_shiraz_2018_075/");
        assertTrue(doc.title().contains("Вино Bin 50 Shiraz"));
    }
}
