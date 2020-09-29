package com.wine.to.up.simple.parser.service.SimpleParser;

import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Parser {
    private final String URL = "https://simplewine.ru";
    private final int PAGES_TO_PARSE = 21; // < 132
    private Document doc;
    private Document wineDocument;
    private ArrayList<String> wineURLs;
    private ArrayList<Wine> WineCatalog;
    // private Logger logger;
    int n_pages;

    public Parser() throws IOException {
        wineURLs = new ArrayList<String>();
        WineCatalog = new ArrayList<Wine>();
        // logger = LoggerFactory.getLogger("ParserLogger");
    }

    public ArrayList<Wine> startParser() throws IOException {
        String name = "";
        String brandID = "";
        String countryID = "";
        float volume = 0;
        String price = "";
        int year = 0;
        float abv = 0;
        String colorType = "";
        String sugarType = "";
        String grapeType = "";
        String[] name_year;

        doc = Jsoup.connect(URL + "/catalog/vino/").get();
        int temp = doc.getElementsByAttributeValue("class", "pagination__navigation").size();
        n_pages = Integer
                .parseInt(doc.getElementsByAttributeValue("class", "pagination__navigation").get(0).child(7).text());

        for (int i = 1; i < PAGES_TO_PARSE; i++) {
            doc = Jsoup.connect(URL + "/catalog/vino/page" + i).get();
            Elements wines = doc.getElementsByAttributeValue("class", "catalog-grid__item");

            for (Element wine : wines) {
                name_year = wine.getElementsByAttributeValue("class", "product-snippet__name").text().split(", ");
                name = name_year[0].replaceFirst("Вино ", "");
                price = wine.getElementsByAttributeValue("class", "product-snippet__price").attr("content");

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
                            volume = Float.parseFloat(wine_Element.child(1).text().split(" ")[0]);
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
                WineCatalog.add(new Wine(name, brandID, countryID, price, volume, abv, colorType, sugarType));
                // logger.info("New wine was added to the catalog");
            }
        }
        return WineCatalog;
    }
}