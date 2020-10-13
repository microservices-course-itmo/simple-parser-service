package com.wine.to.up.simple.parser.service.SimpleParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.wine.to.up.parser.common.api.schema.UpdateProducts;
import com.wine.to.up.simple.parser.service.repository.*;
import org.jsoup.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
// @PropertySource(value = "application.properties")
public class Parser {

    // @Value("${parser.url}")
    private static final String URL = "https://simplewine.ru";
    private static final int PAGES_TO_PARSE = 108; // currently max 132, lower const value for testing purposes
    private static final String HOME_URL = URL + "/catalog/vino/";
    private static final String WINE_URL = URL + "/catalog/vino/page";

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
        DbHandler dbHandler = new DbHandler(grapesRepository, brandsRepository,countriesRepository,wineGrapesRepository,wineRepository);
        CommonDbHandler commonDbHandler = new CommonDbHandler();
        List<UpdateProducts.Product> products = new ArrayList<>();
        UpdateProducts.UpdateProductsMessage message;

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

                    bottlePrice = wine.getElementsByAttributeValue("class", "product-snippet__price").attr("content");
                    // A price check. bottlePrice may be empty if the wine is not in stock
                    if (bottlePrice.isEmpty()) {
                        continue;
                    }

                    String[] NameAndYear = wine.getElementsByAttributeValue("class", "product-snippet__name").text()
                            .split(", ");
                    wineName = NameAndYear[0].replaceFirst("Вино ", "");

                    //the price may not be specified
                    if (NameAndYear.length == 1) {
                        bottleYear = "0";
                    } else {
                        bottleYear = NameAndYear[1];
                    }

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

                    Integer year = Integer.parseInt(bottleYear.split(" ")[0]);
                    Float price = Float.parseFloat(bottlePrice.replace(" ", "").replace("₽", ""));
                    // Integer.parseInt(newWine.bottleDiscount.replace("-","").replace("%", ""));
                    newWine = SimpleWine.builder().name(wineName).brandID(brandID).countryID(countryID)
                            .price(price).year(year).volume(bottleVolume).abv(bottleABV).colorType(colorType)
                            .grapeType(grapeType).sugarType(sugarType).discount(0).build();

                    wineCatalog.add(newWine);
                    dbHandler.putInfoToDB(newWine);

                    //TODO: продуктов больше чем в бд?
                    UpdateProducts.Product newProduct = commonDbHandler.putInfoToCommonDb(newWine);
                    if(!products.contains(newProduct))
                        products.add(newProduct);
                }
            }
            log.info("\tEnd of adding information to the database.");

            //TODO: message to Kafka
            message = UpdateProducts.UpdateProductsMessage.newBuilder().setShopLink(URL).addAllProducts(products).build();
            log.info("End of parsing, {} wines collected", products.size());

        } catch (IOException e) {
            log.error("Catalog main page can`t be reached");
            e.printStackTrace();
            throw e;
        }
        return wineCatalog;
    }
}