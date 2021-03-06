package com.wine.to.up.simple.parser.service.simple_parser;

import com.wine.to.up.parser.common.api.schema.ParserApi;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.Collections;

/**
 * Class for testing {@link Parser}
 */
public class ParserTest {
    /**
     * Testing {@link Parser#parseNumberOfPages(Document)} method<br>
     * Trying to get number of pages from downloaded SimpleWine HTML catalog page with 1, 3, 5 and 107 pages
     *
     * @throws IOException Wrong input for {@link Jsoup#parse(File, String)}
     */

    @ParameterizedTest
    @CsvSource({"Catalog_1_page.html,1",
            "Catalog_150_pages.html,150"})
    public void testParseNumberOfPages(String fileName, int expectedNumOfPages) throws IOException {
        File testCatalogPageFile = new File("src/test/test-resources/" + fileName);
        Document testCatalogPage = Jsoup.parse(testCatalogPageFile, "UTF-8");
        int numberOfPages = Parser.parseNumberOfPages(testCatalogPage);
        Assertions.assertEquals(expectedNumOfPages, numberOfPages);
    }

    /**
     * Testing {@link Parser#parseNumberOfPages(Document)} method<br>
     * Trying to get number of pages from downloaded SimpleWine HTML catalog page with no pages
     *
     * @throws IOException Wrong input for {@link Jsoup#parse(File, String)}
     */
    @Test
    public void testParseNumberOfPagesNoPagesNavigation() throws IOException {
        File testCatalogPageFile = new File("src/test/test-resources/Wine_SimpleWine.html"); //wine page instead of catalog page
        Document testCatalogPage = Jsoup.parse(testCatalogPageFile, "UTF-8");
        int numberOfPages = Parser.parseNumberOfPages(testCatalogPage);
        Assertions.assertEquals(0, numberOfPages);
    }

    /**
     * Testing {@link Parser#parseWine(Document)} method<br>
     * Trying to parse wine from downloaded SimpleWine HTML wine page
     *
     * @throws IOException Wrong input for {@link Jsoup#parse(File, String)}
     */
    @Test
    public void testParseWineHTML() throws IOException {
        File testWinePageFile = new File("src/test/test-resources/Wine_SimpleWine.html");
        Document testWinePage = Jsoup.parse(testWinePageFile, "UTF-8");
        SimpleWine testWine = SimpleWine.builder().
                name("Вино Bin 50 Shiraz").
                brand("Lindeman's").
                country("Австралия").
                newPrice((float) 1054.0).
                year(2018).
                capacity((float) 0.75).
                strength((float) 13.0).
                color(ParserApi.Wine.Color.RED).
                grapeSort(Collections.singleton("шираз")).
                sugar(ParserApi.Wine.Sugar.MEDIUM_DRY).
                discount((float) 15.0).
                region("Южная Австралия").
                link(testWinePage.baseUri()).
                rating((float) 4.4).
                image("https://static.simplewine.ru/upload/iblock/4b6/4b61e9ce6183007cebdafeede881aa43.png@x303").
                gastronomy("Прекрасно в сочетании с жареным ягненком, свининой с овощами и сырами средней выдержки.").
                taste("Вино блестящего фиолетово-красного цвета с яркими ароматами темных спелых ягод, ванили, лакрицы и легкими перечными нотками. " +
                        "Среднетелое, насыщенное и хорошо структурированное во вкусе, с бархатистыми танинами и оттенками черной смородины, сливы и ванили в послевкусии.").
                sparkling(false).
                oldPrice((float) 1240.0).
                build();
        Assertions.assertEquals(testWine.toString(), Parser.parseWine(testWinePage, 1).toString());
    }

    /**
     * Testing {@link Parser#parseWine(Document)} method<br>
     * Trying to parse wine from wrong page<br>
     *
     * @throws Exception                 Wrong input for {@link Jsoup#parse(File, String)}
     * @throws IndexOutOfBoundsException "product__header-russian-name" in input file is absent
     */
    @Test
    public void testParseWineWrongPageFormat() throws Exception {
        File testWinePageFile = new File("src/test/test-resources/Catalog_107_pages.html"); //catalog page instead of wine page
        Document testWinePage = Jsoup.parse(testWinePageFile, "UTF-8");
        Assertions.assertThrows(Exception.class, () ->
                Parser.parseWine(testWinePage, 1)
        );
    }
}
