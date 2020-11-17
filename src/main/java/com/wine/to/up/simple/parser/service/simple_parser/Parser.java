package com.wine.to.up.simple.parser.service.simple_parser;

import com.wine.to.up.parser.common.api.schema.ParserApi;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;
import java.util.Arrays;
import java.util.Map;

import static com.wine.to.up.parser.common.api.schema.ParserApi.Wine.Color.*;
import static com.wine.to.up.parser.common.api.schema.ParserApi.Wine.Color.ORANGE;
import static com.wine.to.up.parser.common.api.schema.ParserApi.Wine.Sugar.*;
import static com.wine.to.up.parser.common.api.schema.ParserApi.Wine.Sugar.SWEET;

/**
 * Parses wine and counts number of pages with wines
 */

@Slf4j
@Component
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Parser {

    /**
     * Parsing number of pages from catalog URL
     *
     * @param mainPage URL of simplewine main page
     * @return Number of pages with wines
     */
    public static int parseNumberOfPages(Document mainPage) {
        int numberOfPages = 0;
        try {
            numberOfPages = Integer.parseInt(mainPage.getElementsByAttributeValue("class", "pagination__navigation")
                    .get(0).children().last().previousElementSibling().text());
        } catch (IndexOutOfBoundsException e) {
            log.error("No pagination__navigation was found on page: " + mainPage.baseUri());
        }
        log.trace("Number of pages to parse: {}", numberOfPages);
        return numberOfPages;
    }

    /**
     * Parsing wine info
     *
     * @param wineDoc Jsoup Document of wine
     * @return wine {@link SimpleWine}
     */
    public static SimpleWine parseWine(Document wineDoc) {

        Map<String, ParserApi.Wine.Sugar> sugarMap = Map.of("сухое", DRY, "полусухое", MEDIUM_DRY, "полусладкое",
                MEDIUM, "сладкое", SWEET);
        Map<String, ParserApi.Wine.Color> colorMap = Map.of("красное", RED, "розовое", ROSE, "белое", WHITE,
                "оранжевое", ORANGE);

        long wineParseStart = System.currentTimeMillis();

        float bottlePrice = 0;
        float bottleDiscount = 0;

        var sw = SimpleWine.builder();

        if (!wineDoc.getElementsByClass("product__header-russian-name").isEmpty()) {
        sw.name(wineDoc.getElementsByClass("product__header-russian-name").get(0).text());
        }

        if (!wineDoc.getElementsByClass("product-card-new__header-info").isEmpty()) {
            sw.name(wineDoc.getElementsByClass("product-card-new__header-info").get(0).text());
        }

        sw.rating(Float.parseFloat(wineDoc.getElementsByClass("ui-rating-stars__value").get(0).text()));
        if (wineDoc.select("img").hasClass("product-slider__slide-img")) {
            sw.image(wineDoc.getElementsByClass("product-slider__slide-img").first().attr("src"));
        }

        log.debug("Fetch wine position page takes : {}", System.currentTimeMillis() - wineParseStart);
        Elements prices = wineDoc.getElementsByClass("product__buy-price");
        if (prices.get(0).childrenSize() > 1) {
            bottlePrice = Float.parseFloat(prices.get(0).child(1).text().replaceAll(" |₽", ""));
            bottleDiscount = Float.parseFloat(prices.get(0).child(2).text().replaceAll("-|%", ""));
            sw.newPrice(bottlePrice);
            sw.discount(bottleDiscount);
        } else {
            bottlePrice = Float.parseFloat(prices.get(0).child(0).text().replaceAll(" |₽", ""));
            bottleDiscount = 0;
            sw.newPrice(bottlePrice);
            sw.discount(bottleDiscount);
        }

        Elements productFacts = wineDoc.getElementsByClass("product__facts-info-text");
        for (Element productFact : productFacts) {
            if (productFact.childrenSize() > 0) {
                String href = productFact.child(0).attr("href");
                String fact = href.split("/")[4].split("(-|_)")[0];
                switch (fact) {
                    case "country":
                        sw.country(productFact.text().split(",")[0]);
                        break;
                    case "color":
                        // colorType = productFact.text();
                        sw.color(colorMap.getOrDefault(productFact.text(), RED));
                        break;
                    case "sugar":
                        // sugarType = productFact.text();
                        sw.sugar(sugarMap.getOrDefault(productFact.text(), DRY));
                        break;
                    case "grape":
                        // grapeType = productFact.text();
                        sw.grapeSort(Arrays.asList(productFact.text().split(", ")));
                        break;
                    case "aging":
                        break;

                    default:
                        break;
                }
            }
        }

        Elements productCharateristics = wineDoc.getElementsByClass("characteristics-params__item");
        for (Element productCharateristic : productCharateristics) {
            String charateristicTitle = productCharateristic.child(0).text();
            switch (charateristicTitle) {
                case "Регион:":
                    sw.region(productCharateristic.child(1).text());
                    break;
                case "Производитель:":
                    sw.brand(productCharateristic.child(1).text());
                    break;
                case "Объем:":
                    // bottleVolume = Float.parseFloat(productCharateristic.child(1).text());
                    sw.capacity(Float.parseFloat(productCharateristic.child(1).text()));
                    break;
                case "Год:":
                    sw.year(Integer.parseInt(productCharateristic.child(1).text()));
                    break;
                case "Крепость:":
                    sw.strength(Float.parseFloat(productCharateristic.child(1).text().replace("%", "")));
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
                    sw.gastronomy(productDescription.child(1).text());
                    break;
                case "Дегустационные характеристики:":
                    sw.taste(productDescription.child(1).text());
                    break;

                default:
                    break;
            }
        }

        log.debug("Wine parsing takes : {}", System.currentTimeMillis() - wineParseStart);

        // return
        // SimpleWine.builder().name(wineName).brand(brandID).country(countryID).newPrice(bottlePrice)
        // .year(bottleYear).capacity(bottleVolume).strength(bottleABV).color(colorMap.getOrDefault(colorType,
        // RED)).grapeSort(Arrays.asList(grapeType.split(", ")))
        // .sugar(sugarMap.getOrDefault(sugarType,
        // DRY)).discount(bottleDiscount).region(region).link(wineDoc.baseUri())
        // .image(bottleImage).rating(wineRating).sparkling(sparkling).taste(wineTaste)
        // .gastronomy(wineGastronomy).oldPrice(100 * bottlePrice / (100 -
        // bottleDiscount)).build();

        return sw.link(wineDoc.baseUri()).discount(bottleDiscount).oldPrice(100 * bottlePrice / (100 - bottleDiscount))
                .build();
    }
}
