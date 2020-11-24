package com.wine.to.up.simple.parser.service.simple_parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

/**
 * Class for testing {@link Parser}
 */
class ParserTest {
    /**
     * SimpleWine base URL
     */
    private static final String URL = "https://simplewine.ru";

    /**
     * Testing {@link Parser#parseNumberOfPages(Document)} method<br>
     * Trying to get number of pages from downloaded SimpleWine HTML catalog page with 1, 3, 5 and 107 pages
     *
     * @throws IOException Wrong input for {@link Jsoup#parse(File, String)}
     */

    @ParameterizedTest
    @CsvSource({"Catalog_1_page.html,1", "Catalog_3_pages.html,3", "Catalog_5_pages.html,5", "Catalog_107_pages.html,107"})
    void testParseNumberOfPages(String fileName, int expectedNumOfPages) throws IOException {
        File testCatalogPageFile = new File("src/test/test-resources/" + fileName);
        Document testCatalogPage = Jsoup.parse(testCatalogPageFile, "UTF-8");
        int numberOfPages = Parser.parseNumberOfPages(testCatalogPage);
        assertEquals(expectedNumOfPages, numberOfPages);
    }

    /**
     * Testing {@link Parser#parseNumberOfPages(Document)} method<br>
     * Trying to get number of pages from downloaded SimpleWine HTML catalog page with no pages
     *
     * @throws IOException Wrong input for {@link Jsoup#parse(File, String)}
     */
    @Test
    void testParseNumberOfPagesNoPagesNavigation() throws IOException {
        File testCatalogPageFile = new File("src/test/test-resources/Wine_SimpleWine.html"); //wine page instead of catalog page
        Document testCatalogPage = Jsoup.parse(testCatalogPageFile, "UTF-8");
        Elements elements = testCatalogPage.getElementsByAttributeValue("class", "pagination__navigation");
        assertTrue(elements.isEmpty()); //"pagination__navigation" in input file is absent
        int numberOfPages = Parser.parseNumberOfPages(testCatalogPage);
        assertEquals(0, numberOfPages);
    }

    // /**
    //  * Testing {@link Parser#parseWine(Document)} method<br>
    //  * Trying to parse wine from downloaded SimpleWine HTML wine page
    //  *
    //  * @throws IOException Wrong input for {@link Jsoup#parse(File, String)}
    //  */
    // @Test
    // void testParseWineHTML() throws IOException {
    //     File testWinePageFile = new File("src/test/test-resources/Wine_SimpleWine.html");
    //     Document testWinePage = Jsoup.parse(testWinePageFile, "UTF-8");
    //     SimpleWine testWine = SimpleWine.builder().
    //             name("Бин 50 Шираз").
    //             brand("Lindeman's").
    //             country("Австралия").
    //             newPrice((float) 952.0).
    //             year(2018).
    //             capacity((float) 0.75).
    //             strength((float) 13.0).
    //             color(ParserApi.Wine.Color.RED).
    //             grapeSort(Collections.singleton("шираз")).
    //             sugar(ParserApi.Wine.Sugar.MEDIUM_DRY).
    //             discount((float) 20.0).
    //             region("Новый Южный Уэльс").
    //             link(testWinePage.baseUri()).
    //             rating((float) 4.6).
    //             image("https://static.simplewine.ru/upload/iblock/3ce/vino-bin-50-shiraz-lindeman-s-2018_1.png@x303").
    //             gastronomy("Прекрасно в сочетании с жареным ягненком, свининой с овощами и сырами средней выдержки.").
    //             taste("Вино блестящего фиолетово-красного цвета с яркими ароматами темных спелых ягод, ванили, лакрицы и легкими перечными нотками. " +
    //                     "Среднетелое, насыщенное и хорошо структурированное во вкусе, с бархатистыми танинами и оттенками черной смородины, сливы и ванили в послевкусии.").
    //             sparkling(false).
    //             oldPrice((float) 1190.0).
    //             build();
    //     assertEquals(testWine.toString(), Parser.parseWine(testWinePage).toString());
    // }

    /**
     * Testing {@link Parser#parseWine(Document)} method<br>
     * Trying to parse wine from wrong page<br>
     *
     * @throws IOException               Wrong input for {@link Jsoup#parse(File, String)}
     * @throws IndexOutOfBoundsException "product__header-russian-name" in input file is absent
     */
    @Test
    void testParseWineWrongPageFormat() throws IOException {
        File testWinePageFile = new File("src/test/test-resources/Catalog_107_pages.html"); //catalog page instead of wine page
        Document testWinePage = Jsoup.parse(testWinePageFile, "UTF-8");
        assertThrows(IndexOutOfBoundsException.class, () ->
                Parser.parseWine(testWinePage)
        );
    }
}
