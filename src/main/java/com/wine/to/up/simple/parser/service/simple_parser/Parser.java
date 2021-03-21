package com.wine.to.up.simple.parser.service.simple_parser;

import com.wine.to.up.simple.parser.service.components.SimpleParserMetricsCollector;

import com.wine.to.up.simple.parser.service.simple_parser.enums.Color;
import com.wine.to.up.simple.parser.service.simple_parser.enums.Sugar;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.wine.to.up.simple.parser.service.logging.SimpleParserNotableEvents.*;

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
            Node scriptElement = mainPage.getElementsByTag("script").get(26).childNode(0);
            Pattern pattern = Pattern.compile("data-total-page=\\\\\"\\d+\\\\\""); //a pattern for string like "data-total-page=\"150\""
            Matcher matcher = pattern.matcher(scriptElement.toString());
            if (matcher.find()) {
                String res = scriptElement.toString().substring(matcher.start(), matcher.end());
                numberOfPages = Integer.parseInt(res.substring(res.indexOf('"') + 1, res.length() - 2)); //to extract a numerical part
            } else {
                log.error("No match for \"data-total-page\" was found on page: {}", mainPage.baseUri());
            }
        } catch (IndexOutOfBoundsException e) {
            log.error("No element with number of pages was found on page: {}", mainPage.baseUri());
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
    public static SimpleWine parseWine(Document wineDoc, int city) {
        long wineParseStart = System.currentTimeMillis();

        float bottlePrice = 0;
        float bottleDiscount = 0;

        SimpleWine.SimpleWineBuilder sw = SimpleWine.builder();

        if (wineDoc.is(":has(.js-offer-reservation-btn)")) { //"js-offer-reservation-btn" is a reservation button that is present only when wine is out of stock
            sw.inStock(0);
        }

        String header = wineDoc.select("h1").get(0).text();
        if (header.matches(".{0,}(Игристое|Шипучее|Шампанское).{0,}")) {
            sw.sparkling(true);
        }

        if (wineDoc.is(":has(.product-card-type-b__header-title)")) {
            sw.name(wineDoc.getElementsByClass("product-card-type-b__header-title").get(0).text().split(",")[0]);
        } else if (wineDoc.is(":has(.product-card-type-a__header-title)")) {
            sw.name(wineDoc.getElementsByClass("product-card-type-a__header-title").get(0).text().split(",")[0]);
        } else {
            log.error("The layout has been changed!");
            ParserService.eventLogger.warn(W_WINE_DETAILS_PARSING_FAILED, wineDoc.baseUri());
        }

        sw.rating(Float.parseFloat(wineDoc.getElementsByClass("product-info__raiting-count").get(0).text()));
        if (wineDoc.select("img").hasClass("product-slider__slide-img")) {
            sw.image(wineDoc.getElementsByClass("product-slider__slide-img").first().attr("src"));
        }

        log.debug("Fetch wine position page takes : {}", System.currentTimeMillis() - wineParseStart);
        Elements prices = wineDoc.getElementsByClass("product-buy__price");
        Elements discounts = wineDoc.getElementsByClass("product-buy__old-price product-buy__with-one");
        if (!prices.get(0).text().equals("")) {
            if (!discounts.isEmpty()) {
                bottlePrice = Float.parseFloat(prices.get(0).text().split("-")[0].replaceAll(" |₽", ""));
                bottleDiscount = Float.parseFloat(prices.get(0).text().split("-")[1].replace("%", ""));
            } else {
                bottlePrice = Float.parseFloat(prices.get(0).text().replaceAll(" |₽", ""));
                bottleDiscount = 0;
            }
        } else {
            bottlePrice = 0;
            bottleDiscount = 0;
            ParserService.eventLogger.warn(W_WINE_ATTRIBUTE_ABSENT, "price", wineDoc.baseUri());
        }
        sw.newPrice(bottlePrice);
        sw.discount(bottleDiscount);

        if (wineDoc.is(":has(.product-info__list-item)")) {
            Elements productFacts = wineDoc.getElementsByClass("product-info__list-item");
            parseProductFacts(sw, productFacts);
        } else {
            ParserService.eventLogger.warn(W_WINE_DETAILS_PARSING_FAILED, wineDoc.baseUri());
        }

        Elements productCharateristics = wineDoc.getElementsByClass("characteristics-params__item");
        parseProductCharateristics(sw, productCharateristics);

        Elements productDescriptions = wineDoc.getElementsByClass("characteristics-description__item");
        parseProductDescriptions(sw, productDescriptions);

        log.debug("Wine parsing takes : {}", System.currentTimeMillis() - wineParseStart);
        SimpleParserMetricsCollector.parseWineDetailsParsing(new Date().getTime() - wineParseStart, city);
        SimpleWine wineRes = sw.link(wineDoc.baseUri()).discount(bottleDiscount).oldPrice(100 * bottlePrice / (100 - bottleDiscount))
                .build();
        checkAbsentFields(wineDoc, wineRes);
        return wineRes;
    }

    private static void parseProductFacts(SimpleWine.SimpleWineBuilder sw, Elements productFacts) {
        for (Element productFact : productFacts) {
            if (productFact.childrenSize() > 0) {
                String fact = productFact.child(0).text();
                switch (fact) {
                    case "Страна, регион:":
                        sw.country(productFact.child(1).text().split(",")[0]);
                        break;
                    case "Вино:":
                        sw.color(Color.getApiColor(productFact.child(1).text()));
                        break;
                    case "Сахар:":
                        sw.sugar(Sugar.getApiSugar(productFact.child(1).text()));
                        break;
                    case "Виноград:":
                        sw.grapeSort(Arrays.asList(productFact.child(1).text().split(", ")));
                        break;
                    case "Производитель:":
                        sw.brand(productFact.child(1).text());
                        break;
                    case "Объем:":
                        sw.capacity(Float.parseFloat(productFact.child(1).text().replaceAll(" |л", "")));
                        break;
                    default:
                        break;
                }
            }
        }
    }

    private static void parseProductCharateristics(SimpleWine.SimpleWineBuilder sw, Elements productCharateristics) {
        for (Element productCharateristic : productCharateristics) {
            String charateristicTitle = productCharateristic.child(0).text();
            switch (charateristicTitle) {
                case "Регион:":
                    sw.region(productCharateristic.child(1).text());
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
    }

    private static void parseProductDescriptions(SimpleWine.SimpleWineBuilder sw, Elements productDescriptions) {
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
    }

    private static void checkAbsentFields(Document wineDoc, SimpleWine wineRes) {
        for (Method m : wineRes.getClass().getMethods()) {
            if (m.getName().startsWith("get") && m.getParameterTypes().length == 0 && !m.getName().endsWith("BrandID") && !m.getName().endsWith("CountryID")) {
                try {
                    if (m.invoke(wineRes) == null) {
                        String fieldName = m.getName().substring(3);
                        ParserService.eventLogger.warn(W_WINE_ATTRIBUTE_ABSENT, fieldName.substring(0, 1).toLowerCase() + fieldName.substring(1), wineDoc.baseUri());
                    }
                } catch (Exception e) {
                    log.warn("Field is not accessible");
                }
            }
        }
    }
}
