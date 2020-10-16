package com.wine.to.up.simple.parser.service.SimpleParser;

import java.io.IOException;
import java.util.ArrayList;

import com.wine.to.up.simple.parser.service.domain.entity.*;
import com.wine.to.up.simple.parser.service.repository.*;
import org.jsoup.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class Parser {
    private static String URL;
    private static final int PAGES_TO_PARSE = 108; // currently max 132, lower const value for testing purposes
    private static String HOME_URL;
    private static String WINE_URL;

    @Value("${parser.url}")
    public void setURLStatic(String URL_FROM_PROPERTY) {
        URL = URL_FROM_PROPERTY;
        HOME_URL = URL + "/catalog/vino/";
        WINE_URL = URL + "/catalog/vino/page";
    }

    @Autowired
    private GrapesRepository grapesRepository;
    @Autowired
    private BrandsRepository brandsRepository;
    @Autowired
    private CountriesRepository countriesRepository;
    @Autowired
    private WineGrapesRepository wineGrapesRepository;
    @Autowired
    private WineRepository wineRepository;

    public ArrayList<SimpleWine> startParser() throws IOException {
        ArrayList<String> wineURLs = new ArrayList<>();
        ArrayList<SimpleWine> wineCatalog = new ArrayList<>();

        int numberOfPages;

        String wineName = "";
        String brandID = "";
        String countryID = "";
        float bottleVolume = 0;
        String bottlePrice = "";
        String bottleDiscount = "";
        String bottleYear = "";
        float bottleABV = 0;
        String colorType = "";
        String sugarType = "";
        String grapeType = "";

        SimpleWine newWine;

        try {
            log.info("URL = " + URL);
            Document doc = Jsoup.connect(HOME_URL).get();

            numberOfPages = Integer.parseInt(
                    doc.getElementsByAttributeValue("class", "pagination__navigation").get(0).child(7).text());

            log.info("\tStart of adding information to the database.");
            for (int i = 1; i < PAGES_TO_PARSE; i++) {
                try {
                    doc = Jsoup.connect(WINE_URL + i).get();
                } catch (IOException e) {
                    log.error(WINE_URL + i + " can`t be reached");
                    continue;
                }
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

                    // newWine = new SimpleWine(wineName, brandID, countryID, bottlePrice,
                    // bottleVolume, bottleABV, colorType,
                    // sugarType);
                    int year = 0; //wasn't parsed in spring-2
                    newWine = SimpleWine.builder().name(wineName).brandID(brandID).countryID(countryID)
                            .price(bottlePrice).year(year).volume(bottleVolume).abv(bottleABV).colorType(colorType)
                            .grapeType(grapeType).sugarType(sugarType).build();

                    wineCatalog.add(newWine);

                    // A price check. bottlePrice may be empty if the wine is not in stock
                    if (!bottlePrice.isEmpty())
                        putInfoToDB(newWine);
                }
            }
            log.info("\tEnd of adding information to the database.");
        } catch (IOException e) {
            log.error("Catalog main page can`t be reached");
            e.printStackTrace();
            throw e;
        }
        return wineCatalog;
    }

    private void putInfoToDB(SimpleWine newWine) {
        Countries countryEntity;
        if (!countriesRepository.existsCountriesByCountryName(newWine.getCountryID())) {
            countryEntity = new Countries(newWine.getCountryID());
            countriesRepository.save(countryEntity);
            log.info("New Country was added to DB: " + newWine.getCountryID());
        } else {
            countryEntity = countriesRepository.findCountryByCountryName(newWine.getCountryID());
        }
        Brands brandEntity;
        if (!brandsRepository.existsBrandsByBrandName(newWine.getBrandID())) {
            brandEntity = new Brands(newWine.getBrandID());
            brandsRepository.save(brandEntity);
            log.info("New Brand was added to DB: " + newWine.getBrandID());
        } else {
            brandEntity = brandsRepository.findBrandByBrandName(newWine.getBrandID());
        }
        Grapes grapeEntity;
        if (!grapesRepository.existsGrapesByGrapeName(newWine.getGrapeType())) {
            grapeEntity = new Grapes(newWine.getGrapeType());
            grapesRepository.save(grapeEntity);
            log.info("New Grape was added to DB: " + newWine.getGrapeType());
        } else {
            grapeEntity = grapesRepository.findGrapeByGrapeName(newWine.getGrapeType());
        }
        Float price = Float.parseFloat(newWine.getPrice().replace(" ", "").replace("₽", ""));
        // int year = Integer.parseInt(newWine.bottleYear.replace(" г.", ""));
        // int discount =
        // Integer.parseInt(newWine.bottleDiscount.replace("-","").replace("%", ""));
        Wine wineEntity;
        if (!wineRepository.existsWineByNameAndPriceAndVolumeAndColorTypeAndSugarType(newWine.getName(), price,
                newWine.getVolume(), newWine.getColorType(), newWine.getSugarType())) {
            wineEntity = new Wine(newWine.getName(), brandEntity, countryEntity, price, newWine.getVolume(),
                    newWine.getAbv(), newWine.getColorType(), newWine.getSugarType(), newWine.getGrapeType());
            wineRepository.save(wineEntity);
            log.info("New Wine was added to DB: " + wineEntity.toString());
        } else {
            wineEntity = wineRepository.findWineByNameAndPriceAndVolumeAndColorTypeAndSugarType(newWine.getName(),
                    price, newWine.getVolume(), newWine.getColorType(), newWine.getSugarType());
        }
        WineGrapes wineGrapeEntity;
        if (!wineGrapesRepository.existsWineGrapesByGrapeIdAndAndWineId(grapeEntity, wineEntity)) {
            wineGrapeEntity = new WineGrapes(wineEntity, grapeEntity);
            wineGrapesRepository.save(wineGrapeEntity);
            log.info("New Connection between Wine and Grape was added to DB");
        }
    }
}