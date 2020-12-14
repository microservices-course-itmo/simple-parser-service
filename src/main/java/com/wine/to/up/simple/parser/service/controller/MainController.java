package com.wine.to.up.simple.parser.service.controller;

import com.wine.to.up.parser.common.api.schema.ParserApi;
import com.wine.to.up.simple.parser.service.simple_parser.ParserService;
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

    /**
     * The repository that stores grapes (name, id) info. It's autowired by Spring.
     */
    private GrapesRepository grapesRepository;

    /**
     * The repository that stores brands info (name, id). It's autowired by Spring.
     */
    private BrandsRepository brandsRepository;

    /**
     * The repository that stores countries info (name, id). It's autowired by Spring.
     */
    private CountriesRepository countriesRepository;

    /**
     * The repository that stores all info about wine. It's autowired by Spring.
     */
    private WineRepository wineRepository;

    /**
     * The service that is responsible for parser start up. It's autowired by Spring.
     */
    private ParserService parserService;

    @Autowired
    public MainController(GrapesRepository grapesRepository, BrandsRepository brandsRepository, CountriesRepository countriesRepository, WineRepository wineRepository, ParserService parserService) {
        this.grapesRepository = grapesRepository;
        this.brandsRepository = brandsRepository;
        this.countriesRepository = countriesRepository;
        this.wineRepository = wineRepository;
        this.parserService = parserService;
    }

    /**
     * The method based on a GET request. The parser runs when the request is received.
     *
     * @return message about successful execution.
     */
    @GetMapping(path = "/run-parser")
    public String runParser(@RequestParam int pagesToParse, @RequestParam int sparklingPagesToParse) {
        parserService.startParser(pagesToParse, sparklingPagesToParse);
        return "Parser started by request";
    }
    @GetMapping(path = "/run-parser/one_page")
    public String runParserOnePage() {
        parserService.startParser(1,0);
        return "Parser started by request";
    }

    /**
     * The method based on a GET request. Output of all grape types stored in the grapesRepository {@link GrapesRepository}.
     *
     * @return list of all grape types as HTML
     */
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

    /**
     * The method based on a GET request. Output of all brands stored in the {@link BrandsRepository}.
     *
     * @return list of all brands as HTML
     */
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

    /**
     * The method based on a GET request. Output of all countries stored in the {@link CountriesRepository}.
     *
     * @return list of all countries as HTML
     */
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

    /**
     * A method based on a GET request. Output of all wines stored in the {@link WineRepository}.
     *
     * @return list of all wines as HTML
     */
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

    /**
     * The method based on a GET request. Output of a message created to be sent to Kafka, contains all parsed wine information.
     *
     * @return list of all parsed wine info as HTML
     */
    @GetMapping(path = "/all-products")
    @ResponseBody
    public String getAllProducts() {
        ParserApi.WineParsedEvent message = parserService.getMessage();
        StringBuilder html = new StringBuilder();
        for (ParserApi.Wine someProduct : message.getWinesList()) {
            html.append("<a>Name: </a>").append(someProduct.getName()).append("<br>");
            html.append("<a>Link: </a>").append(someProduct.getLink()).append("<br>");
            html.append("<a>Brand: </a>").append(someProduct.getBrand()).append("<br>");
            html.append("<a>Country: </a>").append(someProduct.getCountry()).append("<br>");
            html.append("<a>Region: </a>").append(someProduct.getRegion(0)).append("<br>");
            html.append("<a>Year: </a>").append(someProduct.getYear()).append("<br>");
            html.append("<a>Grapes: </a>").append(someProduct.getGrapeSortList()).append("<br>");
            html.append("<a>Volume: </a>").append(someProduct.getCapacity()).append("<br>");
            html.append("<a>ABV: </a>").append(someProduct.getStrength()).append("<br>");
            html.append("<a>Sugar: </a>").append(someProduct.getSugar()).append("<br>");
            html.append("<a>Color: </a>").append(someProduct.getColor()).append("<br>");
            html.append("<a>New Price: </a>").append(someProduct.getNewPrice()).append("<br>");
            html.append("<a>Old Price: </a>").append(someProduct.getOldPrice()).append("<br>");
            html.append("<a>Sparkling: </a>").append(someProduct.getSparkling()).append("<br><br>");
        }
        return html.toString();
    }


    /**
     * Parser's homepage which contains links to some methods.
     *
     * @return HTML page with links
     */
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