package com.wine.to.up.simple.parser.service.SimpleParser;

import java.io.IOException;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@NoArgsConstructor
public class Parser {
    private static String URL;
    private static final int PAGES_TO_PARSE = 108; // currently max 132, lower const value for testing purposes
    public static String HOME_URL;
    private static String WINE_URL;

    @Value("${parser.url}")
    public void setURLStatic(String URL_FROM_PROPERTY) {
        URL = URL_FROM_PROPERTY;
        HOME_URL = URL + "/catalog/vino/";
        WINE_URL = URL + "/catalog/vino/page";
    }

    public static Document URLToDocument(String someURL) throws IOException {
        return Jsoup.connect(someURL).get();
    }

    protected int parseNumberOfPages(Document mainPage) {
        int numberOfPages = 0;
        try {
            numberOfPages = Integer.parseInt(
                    mainPage.getElementsByAttributeValue("class", "pagination__navigation").get(0).children().last()
                            .previousElementSibling().text()); // works for catalogs with 7 or less pages
        } catch (IndexOutOfBoundsException e) {
            log.error("No pagination__navigation was found on page: " + mainPage.baseUri());
        }

        log.trace("Number of pages to parse: {}", numberOfPages);
        return numberOfPages;
    }

    public static SimpleWine parseWine(Document wineDoc) {
        long wineParseStart = System.currentTimeMillis();
        String wineName = "";
        String brandID = "";
        String countryID = "";
        float bottleVolume = 0;
        float bottlePrice = 0;
        float bottleDiscount = 0;
        int bottleYear = 0;
        float bottleABV = 0;
        String colorType = "";
        String sugarType = "";
        String grapeType = "";
        String region = "";
        float wineRating = 0;
        String bottleImage = "";
        boolean sparkling = false;
        String wineGastronomy = "";
        String wineTaste = "";

        wineName = wineDoc.getElementsByClass("product__header-russian-name").get(0).text();
        wineRating = Float.parseFloat(wineDoc.getElementsByClass("ui-rating-stars__value").get(0).text());
        bottleImage = wineDoc.getElementsByClass("product-slider__slide-img").first().attr("src");

        log.debug("Fetch wine position page takes : {}", System.currentTimeMillis() - wineParseStart);
        Elements prices = wineDoc.getElementsByClass("product__buy-price");
        if (prices.get(0).childrenSize() > 1) {
            bottlePrice = Float.parseFloat(prices.get(0).child(1).text().replaceAll(" |₽", ""));
            bottleDiscount = Float.parseFloat(prices.get(0).child(2).text().replaceAll("-|%", ""));
        } else {
            bottlePrice = Float.parseFloat(prices.get(0).child(0).text().replaceAll(" |₽", ""));
            bottleDiscount = 0;
        }

        Elements productFacts = wineDoc.getElementsByClass("product__facts-info-text");
        for (Element productFact : productFacts) {
            if (productFact.childrenSize() > 0) {
                String href = productFact.child(0).attr("href");
                String fact = href.split("/")[4].split("(-|_)")[0];
                switch (fact) {
                    case "country":
                        countryID = productFact.text().split(",")[0];
                        break;
                    case "color":
                        colorType = productFact.text();
                        break;
                    case "sugar":
                        sugarType = productFact.text();
                        break;
                    case "grape":
                        grapeType = productFact.text();
                        break;
                    case "aging":
                        // grapeType = productFact.text();
                        break;

                    default:
                        break;

                }
            }
        }

        Elements productCharateristics = wineDoc.getElementsByClass("characteristics-params__item");
        for (Element productCharateristic : productCharateristics) {
            String charateristicTitle = productCharateristic.child(0).text();
            // System.out.println(charateristicTitle);
            switch (charateristicTitle) {
                case "Регион:":
                    region = productCharateristic.child(1).text();
                    break;
                case "Производитель:":
                    brandID = productCharateristic.child(1).text();
                    break;
                case "Объем:":
                    bottleVolume = Float.parseFloat(productCharateristic.child(1).text());
                    break;
                case "Год:":
                    bottleYear = Integer.parseInt(productCharateristic.child(1).text());
                    break;
                case "Крепость:":
                    bottleABV = Float.parseFloat(productCharateristic.child(1).text().replaceAll("%", ""));
                    break;

                default:
                    break;

            }
        }

        Elements productDescriptions = wineDoc.getElementsByClass("characteristics-description__item");
        for (Element productDescription : productDescriptions) {
            String descriptionItem = productDescription.children().first().text();

            switch (descriptionItem) {
                case "Гастрономия:":
                    wineGastronomy = productDescription.child(1).text();
                    break;
                case "Дегустационные характеристики:":
                    wineTaste = productDescription.child(1).text();
                    break;

                default:
                    break;

            }

        }

        log.debug("Wine parsing takes : {}", System.currentTimeMillis() - wineParseStart);

        return SimpleWine.builder().name(wineName).brandID(brandID).countryID(countryID).price(bottlePrice)
                .year(bottleYear).volume(bottleVolume).abv(bottleABV).colorType(colorType).grapeType(grapeType)
                .sugarType(sugarType).discount(bottleDiscount).region(region).link(wineDoc.baseUri()).build();

    }
}
