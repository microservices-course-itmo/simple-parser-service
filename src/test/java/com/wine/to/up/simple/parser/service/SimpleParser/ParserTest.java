package com.wine.to.up.simple.parser.service.SimpleParser;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.*;

import static org.junit.Assert.*;

import java.io.IOException;


public class ParserTest {

    private Parser parser;
    String URL = "https://simplewine.ru";


    @Before
    public void beforeTest() {
        parser = new Parser();
    }

    @Test
    public void testParseNumberOfPages() throws IOException {
        String TEST_HOME_URL = URL + "/catalog/vino/";
        Document mainPage = Jsoup.connect(TEST_HOME_URL).get();
        assertEquals(Integer.parseInt(mainPage.getElementsByAttributeValue("class", "pagination__navigation").get(0).child(7).text()),
                parser.parseNumberOfPages());
        assertTrue(parser.parseNumberOfPages() > 0);
    }

    @Test
    public void testParseWine() throws IOException {
        SimpleWine testWine = SimpleWine.builder().name("Бин 50 Шираз").brandID("Lindeman's").countryID("Австралия").price((float) 952.0)
                .year(2018).volume((float) 0.75).abv((float) 13.0).colorType("красное").grapeType("шираз")
                .sugarType("полусухое").discount((float) 20.0).build();
        String TEST_WINE_URL = URL + "/catalog/product/lindeman_s_bin_50_shiraz_2018_075/";
        assertEquals(testWine.toString(), Parser.parseWine(TEST_WINE_URL).toString());
    }
}
