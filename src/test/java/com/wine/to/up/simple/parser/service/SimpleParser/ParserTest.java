package com.wine.to.up.simple.parser.service.SimpleParser;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.*;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;


public class ParserTest {

    private static Parser parser;
    private static final String URL = "https://simplewine.ru";


    @BeforeClass
    public static void beforeTest() {
        parser = new Parser();
        ReflectionTestUtils.setField(parser, "URL", URL);
        ReflectionTestUtils.setField(parser, "HOME_URL", URL + "/catalog/vino/");
        ReflectionTestUtils.setField(parser, "WINE_URL", URL + "/catalog/vino/page");
    }

    @Test
    public void testParseNumberOfPages() throws IOException {
        File testCatalogPageFile = new File("src/test/test-resources/Catalog_SimpleWine.html");
        Document testCatalogPage = Jsoup.parse(testCatalogPageFile, "UTF-8");
        int numberOfPages = parser.parseNumberOfPages(testCatalogPage);
        assertEquals(107, numberOfPages);
        assertTrue(numberOfPages > 0);
    }

    @Test
    public void testParseWine() throws IOException {
        File testWinePageFile = new File("src/test/test-resources/Wine_ SimpleWine.html");
        Document testWinePage = Jsoup.parse(testWinePageFile, "UTF-8");
        SimpleWine testWine = SimpleWine.builder().name("Бин 50 Шираз").brandID("Lindeman's").countryID("Австралия").price((float) 952.0)
                .year(2018).volume((float) 0.75).abv((float) 13.0).colorType("красное").grapeType("шираз")
                .sugarType("полусухое").discount((float) 20.0).build();
        assertEquals(testWine.toString(), Parser.parseWine(testWinePage).toString());
    }
}
