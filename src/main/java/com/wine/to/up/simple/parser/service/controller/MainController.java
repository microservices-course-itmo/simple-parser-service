package com.wine.to.up.simple.parser.service.controller;

import com.wine.to.up.parser.common.api.schema.UpdateProducts;
import com.wine.to.up.simple.parser.service.SimpleParser.ParserService;
import com.wine.to.up.simple.parser.service.domain.entity.*;
import com.wine.to.up.simple.parser.service.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller that processes user requests to parser and to DB
 */
@RestController
@RequiredArgsConstructor
@Validated
@Slf4j
@RequestMapping(path = "/simple-parser")
public class MainController {

    /** The repository that stores grapes (name, id) info. It's autowired by Spring.*/
    @Autowired
    private GrapesRepository grapesRepository;

    /** The repository that stores brands info (name, id). It's autowired by Spring.*/
    @Autowired
    private BrandsRepository brandsRepository;

    /** The repository that stores countries info (name, id). It's autowired by Spring.*/
    @Autowired
    private CountriesRepository countriesRepository;

    /** The repository that stores the connections between wine and its grapes. It's autowired by Spring.*/
    @Autowired
    private WineGrapesRepository wineGrapesRepository;

    /** The repository that stores all info about wine. It's autowired by Spring.*/
    @Autowired
    private WineRepository wineRepository;

    /** The service that is responsible for parser start up. It's autowired by Spring.*/
    @Autowired
    private ParserService parserService;

    /** The method based on a GET request. The parser runs when the request is received.
     * @return message about successful execution. */
    @GetMapping(path = "/run-parser")
    public String runParser() {
        parserService.startParser();
        return "Parser started by request";
    }

    /** The method based on a POST request. A new grape type is added to the database when the request is received.
     * @param grape Grape type model. All arguments will be taken from this model.
     * @return instance of the Grape entity.
     * @see Grapes
     */
    @PostMapping(path = "/grape")
    @ResponseBody
    public Grapes addGrape(@ModelAttribute("grape") Grapes grape) {
        Grapes newGrape = new Grapes(grape.getGrapeName());
        grapesRepository.save(newGrape);
        return newGrape;
    }

    /** The method based on a POST request. A new brand is added to the database when the request is received.
     * @param brand Brand model. All arguments will be taken from this model.
     * @return instance of the Brand entity.
     * @see Brands
     */
    @PostMapping(path = "/brand")
    @ResponseBody
    public Brands addBrand(@ModelAttribute("brand") Brands brand) {
        Brands newBrand = new Brands(brand.getBrandName());
        brandsRepository.save(newBrand);
        return newBrand;
    }

    /** The method based on a POST request. A new country is added to the database when the request is received.
     * @param country Country model. All arguments will be taken from this model.
     * @return instance of the Countries entity.
     * @see Countries
     */
    @PostMapping(path = "/country")
    @ResponseBody
    public Countries addCountry(@ModelAttribute("country") Countries country) {
        Countries newCountry = new Countries(country.getCountryName());
        countriesRepository.save(newCountry);
        return newCountry;
    }

    /** The method based on a POST request. All info about new wine is added to the database when the request is received.
     * @param wine Wine type model. All arguments will be taken from this model.
     * @return instance of the Wine entity.
     * @see Wine
     */
    @PostMapping(path = "/wine")
    @ResponseBody
    public Wine addWine(@ModelAttribute("wine") Wine wine) {
        Wine newWine = wine;
        if (Boolean.TRUE.equals(brandsRepository.existsBrandsByBrandName(wine.getBrandID().getBrandName()))) {
            newWine.setBrandID(brandsRepository.findBrandByBrandName(wine.getBrandID().getBrandName()));
        } else {
            brandsRepository.save(newWine.getBrandID());
        }

        if (Boolean.TRUE.equals(countriesRepository.existsCountriesByCountryName(wine.getCountryID().getCountryName()))) {
            newWine.setCountryID(countriesRepository.findCountryByCountryName(wine.getCountryID().getCountryName()));
        } else {
            countriesRepository.save(newWine.getCountryID());
        }

        Grapes grapes;
        if (Boolean.TRUE.equals(grapesRepository.existsGrapesByGrapeName(wine.getGrapeType()))) {
            grapes = grapesRepository.findGrapeByGrapeName(wine.getGrapeType());
        } else {
            grapes = new Grapes(wine.getGrapeType());
            grapesRepository.save(grapes);
        }
        wineRepository.save(newWine);
        wineGrapesRepository.save(new WineGrapes(newWine, grapes));

        return wine;
    }

    /** The method based on a GET request. Output of all grape types stored in the grapesRepository {@link GrapesRepository}.
     * @return list of all grape types as HTML*/
    @GetMapping(path = "/all-grapes")
    @ResponseBody
    public String getAllGrapes() {
        Iterable<Grapes> grapes = grapesRepository.findAll();
        StringBuilder html = new StringBuilder();
        for (Grapes someGrape : grapes) {
            html.append(someGrape).append("<br>");
        }
        return html.toString();
    }

    /** The method based on a GET request. Output of all brands stored in the {@link BrandsRepository}.
     * @return list of all brands as HTML*/
    @GetMapping(path = "/all-brands")
    @ResponseBody
    public String getAllBrands() {
        Iterable<Brands> brands = brandsRepository.findAll();
        StringBuilder html = new StringBuilder();
        for (Brands someBrand : brands) {
            html.append(someBrand).append("<br>");
        }
        return html.toString();
    }

    /** The method based on a GET request. Output of all countries stored in the {@link CountriesRepository}.
     * @return list of all countries as HTML*/
    @GetMapping(path = "/all-countries")
    @ResponseBody
    public String getAllCountries() {
        Iterable<Countries> countries = countriesRepository.findAll();
        StringBuilder html = new StringBuilder();
        for (Countries someCountry : countries) {
            html.append(someCountry).append("<br>");
        }
        return html.toString();
    }

    /** A method based on a GET request. Output of all wines stored in the {@link WineRepository}.
     * @return list of all wines as HTML*/
    @GetMapping(path = "/all-wines")
    @ResponseBody
    public String getAllWines() {
        Iterable<Wine> wines = wineRepository.findAll();
        StringBuilder html = new StringBuilder();
        for (Wine someWine : wines) {
            html.append(someWine).append("<br>");
        }
        return html.toString();
    }

    /** The method based on a GET request. Output of a message created to be sent to Kafka, contains all parsed wine information.
     * @return list of all parsed wine info as HTML*/
    @GetMapping(path = "/all-products")
    @ResponseBody
    public String getAllProducts() {
        UpdateProducts.UpdateProductsMessage message = parserService.getMessage();
        StringBuilder html = new StringBuilder();
        for (UpdateProducts.Product someProduct : message.getProductsList()) {
            html.append("<a>Name: </a>").append(someProduct.getName()).append("<br>");
            html.append("<a>Link: </a>").append(someProduct.getLink()).append("<br>");
            html.append("<a>Brand: </a>").append(someProduct.getBrand()).append("<br>");
            html.append("<a>Country: </a>").append(someProduct.getCountry()).append("<br>");
            html.append("<a>Region: </a>").append(someProduct.getRegion(0)).append("<br>");
            html.append("<a>Year: </a>").append(someProduct.getYear()).append("<br>");
            html.append("<a>Grapes: </a>").append(someProduct.getGrapeSort(0)).append("<br>");
            html.append("<a>Volume: </a>").append(someProduct.getCapacity()).append("<br>");
            html.append("<a>ABV: </a>").append(someProduct.getStrength()).append("<br>");
            html.append("<a>Sugar: </a>").append(someProduct.getSugar()).append("<br>");
            html.append("<a>Color: </a>").append(someProduct.getColor()).append("<br>");
            html.append("<a>New Price: </a>").append(someProduct.getNewPrice()).append("<br>");
            html.append("<a>Old Price: </a>").append(someProduct.getOldPrice()).append("<br><br>");
        }
        return html.toString();
    }

    /** Parser's homepage which contains links to some methods.
     * @return HTML page with links*/
    @ResponseBody
    @GetMapping(path = "/")
    public String home() {
        String html = "";
        html += "<ul>";
        html += " <li><a href='/simple-parser/run-parser'>Run parser</a></li>";
        html += " <li><a href='/simple-parser/all-wines'>Show All Wines</a></li>";
        html += " <li><a href='/simple-parser/all-countries'>Show All Countries</a></li>";
        html += " <li><a href='/simple-parser/all-brands'>Show All Brands</a></li>";
        html += " <li><a href='/simple-parser/all-grapes'>Show All Grapes</a></li>";
        html += " <li><a href='/simple-parser/all-products'>Show All Products as a Message to Kafka</a></li>";
        html += "</ul>";
        return html;
    }
}