package com.wine.to.up.simple.parser.service.SimpleParser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.*;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

/**
 * Class for testing {@link Parser}
 */
public class ParserTest {
    /**
     * SimpleWine base URL
     */
    private static final String URL = "https://simplewine.ru";

    /**
     * Testing {@link Parser#parseNumberOfPages(Document)} method<br>
     * Trying to get number of pages from SimpleWine catalog page by URL<br>
     * Using {@link ParserService#URLToDocument(String)} to get Document from URL
     *
     * @throws IOException Wrong input for {@link ParserService#URLToDocument(String)}
     */
    @Test
    public void testParseNumberOfPagesIntegration() throws IOException {
        Document testCatalogPage = ParserService.URLToDocument(URL + "/catalog/vino/");
        int numberOfPages = Parser.parseNumberOfPages(testCatalogPage);
        assertTrue(numberOfPages >= 0);
    }

    /**
     * Testing {@link Parser#parseNumberOfPages(Document)} method<br>
     * Trying to get number of pages from downloaded SimpleWine HTML catalog page with 1 page
     *
     * @throws IOException Wrong input for {@link Jsoup#parse(File, String)}
     */
    @Test
    public void testParseNumberOfPages1() throws IOException {
        File testCatalogPageFile = new File("src/test/test-resources/Catalog_1_page.html");
        Document testCatalogPage = Jsoup.parse(testCatalogPageFile, "UTF-8");
        int numberOfPages = Parser.parseNumberOfPages(testCatalogPage);
        assertEquals(1, numberOfPages);
    }

    /**
     * Testing {@link Parser#parseNumberOfPages(Document)} method<br>
     * Trying to get number of pages from downloaded SimpleWine HTML catalog page with 3 pages
     *
     * @throws IOException Wrong input for {@link Jsoup#parse(File, String)}
     */
    @Test
    public void testParseNumberOfPages3() throws IOException {
        File testCatalogPageFile = new File("src/test/test-resources/Catalog_3_pages.html");
        Document testCatalogPage = Jsoup.parse(testCatalogPageFile, "UTF-8");
        int numberOfPages = Parser.parseNumberOfPages(testCatalogPage);
        assertEquals(3, numberOfPages);
    }

    /**
     * Testing {@link Parser#parseNumberOfPages(Document)} method<br>
     * Trying to get number of pages from downloaded SimpleWine HTML catalog page with 5 pages
     *
     * @throws IOException Wrong input for {@link Jsoup#parse(File, String)}
     */
    @Test
    public void testParseNumberOfPages5() throws IOException {
        File testCatalogPageFile = new File("src/test/test-resources/Catalog_5_pages.html");
        Document testCatalogPage = Jsoup.parse(testCatalogPageFile, "UTF-8");
        int numberOfPages = Parser.parseNumberOfPages(testCatalogPage);
        assertEquals(5, numberOfPages);
    }

    /**
     * Testing {@link Parser#parseNumberOfPages(Document)} method<br>
     * Trying to get number of pages from downloaded SimpleWine HTML catalog page with 107 pages
     *
     * @throws IOException Wrong input for {@link Jsoup#parse(File, String)}
     */
    @Test
    public void testParseNumberOfPages107() throws IOException {
        File testCatalogPageFile = new File("src/test/test-resources/Catalog_107_pages.html");
        Document testCatalogPage = Jsoup.parse(testCatalogPageFile, "UTF-8");
        int numberOfPages = Parser.parseNumberOfPages(testCatalogPage);
        assertEquals(107, numberOfPages);
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
        Elements elements = testCatalogPage.getElementsByAttributeValue("class", "pagination__navigation");
        assertTrue(elements.isEmpty()); //"pagination__navigation" in input file is absent
        int numberOfPages = Parser.parseNumberOfPages(testCatalogPage);
        assertEquals(0, numberOfPages);
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
                name("Бин 50 Шираз").
                brandID("Lindeman's").
                countryID("Австралия").
                price((float) 952.0).
                year(2018).
                volume((float) 0.75).
                abv((float) 13.0).
                colorType("красное").
                grapeType("шираз").
                sugarType("полусухое").
                discount((float) 20.0).
                region("Новый Южный Уэльс").
                link(testWinePage.baseUri()).
                rating((float) 4.6).
                picture("https://static.simplewine.ru/upload/iblock/3ce/vino-bin-50-shiraz-lindeman-s-2018_1.png@x303").
                gastronomy("Прекрасно в сочетании с жареным ягненком, свининой с овощами и сырами средней выдержки.").
                taste("Вино блестящего фиолетово-красного цвета с яркими ароматами темных спелых ягод, ванили, лакрицы и легкими перечными нотками. " +
                        "Среднетелое, насыщенное и хорошо структурированное во вкусе, с бархатистыми танинами и оттенками черной смородины, сливы и ванили в послевкусии.").
                sparkling(false).
                build();
        assertEquals(testWine.toString(), Parser.parseWine(testWinePage).toString());
    }

    /**
     * Testing {@link Parser#parseWine(Document)} method<br>
     * Trying to parse wine from wrong page<br>
     *
     * @throws IOException               Wrong input for {@link Jsoup#parse(File, String)}
     * @throws IndexOutOfBoundsException "product__header-russian-name" in input file is absent
     */
    @Test
    public void testParseWineWrongPageFormat() throws IOException {
        File testWinePageFile = new File("src/test/test-resources/Catalog_107_pages.html"); //catalog page instead of wine page
        Document testWinePage = Jsoup.parse(testWinePageFile, "UTF-8");
        assertThrows(IndexOutOfBoundsException.class, () ->
                Parser.parseWine(testWinePage)
        );
    }
}
