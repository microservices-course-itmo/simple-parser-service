package com.wine.to.up.simple.parser.service.SimpleParser;

import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Parser {
    private final String URL = "https://simplewine.ru";
    private final int PAGES_TO_PARSE = 21; // currently max 132, lower const value for testing purposes
    private Document doc;
    private Document wineDocument;
    private ArrayList<String> wineURLs;
    private ArrayList<Wine> wineCatalog;
    private int numberOfPages;

    public Parser() {
        wineURLs = new ArrayList<String>();
        wineCatalog = new ArrayList<Wine>();
    }

    public ArrayList<Wine> startParser() throws IOException {
        String wineName = "";
        String brandID = "";
        String countryID = "";
        float bottleVolume = 0;
        String bottlePrice = "";
        int bottleYear = 0;
        float bottleABV = 0;
        String colorType = "";
        String sugarType = "";
        String grapeType = "";

        doc = Jsoup.connect(URL + "/catalog/vino/").get();
        numberOfPages = Integer
                .parseInt(doc.getElementsByAttributeValue("class", "pagination__navigation").get(0).child(7).text());

        for (int i = 1; i < PAGES_TO_PARSE; i++) {
            doc = Jsoup.connect(URL + "/catalog/vino/page" + i).get();
            Elements wines = doc.getElementsByAttributeValue("class", "catalog-grid__item");

            for (Element wine : wines) {
                String[] NameAndYear = wine.getElementsByAttributeValue("class", "product-snippet__name").text()
                        .split(", ");
                wineName = NameAndYear[0].replaceFirst("Вино ", "");
                bottlePrice = wine.getElementsByAttributeValue("class", "product-snippet__price").attr("content");

                Elements wine_Elements = wine.getElementsByAttributeValue("class", "product-snippet__info-item");

                for (Element wine_Element : wine_Elements) {

                    switch (wine_Element.child(0).text()) {
                        case "Страна:":
                            countryID = wine_Element.child(1).text();
                            break;
                        case "Цвет:":
                            colorType = wine_Element.child(1).text();
                            break;
                        case "Сахар:":
                            sugarType = wine_Element.child(1).text();
                            break;
                        case "Объем:":
                            bottleVolume = Float.parseFloat(wine_Element.child(1).text().split(" ")[0]);
                            break;
                        case "Производитель:":
                            brandID = wine_Element.child(1).text();
                            break;
                        case "Виноград:":
                            grapeType = wine_Element.child(1).text();
                            break;

                        default:
                            break;

                    }
                }
                wineURLs.add(wine.getElementsByAttributeValue("class", "product-snippet__name").attr("href"));
                wineCatalog.add(new Wine(wineName, brandID, countryID, bottlePrice, bottleVolume, bottleABV, colorType,
                        sugarType));
                log.info("New wine was added to the catalog");
            }
        }
        return wineCatalog;
    }
}