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

@RestController
@RequiredArgsConstructor
@Validated
@Slf4j
@RequestMapping(path = "/simple-parser")
public class MainController {

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
    @Autowired
    private ParserService parserService;


    @GetMapping(path = "/run-parser")
    public String runParser() {
        parserService.startParser();
        return "Parser started by request";
    }

    @PostMapping(path = "/grape")
    @ResponseBody
    public Grapes addGrape(@ModelAttribute("grape") Grapes grape) {
        Grapes newGrape = new Grapes(grape.getGrapeName());
        grapesRepository.save(newGrape);
        return newGrape;
    }

    @PostMapping(path = "/brand")
    @ResponseBody
    public Brands addBrand(@ModelAttribute("brand") Brands brand) {
        Brands newBrand = new Brands(brand.getBrandName());
        brandsRepository.save(newBrand);
        return newBrand;
    }

    @PostMapping(path = "/country")
    @ResponseBody
    public Countries addCountry(@ModelAttribute("country") Countries country) {
        Countries newCountry = new Countries(country.getCountryName());
        countriesRepository.save(newCountry);
        return newCountry;
    }

    @PostMapping(path = "/wine")
    @ResponseBody
    public Wine addWine(@ModelAttribute("wine") Wine wine) {
        Wine newWine = wine;
        if (brandsRepository.existsBrandsByBrandName(wine.getBrandID().getBrandName())) {
            newWine.setBrandID(brandsRepository.findBrandByBrandName(wine.getBrandID().getBrandName()));
        } else {
            brandsRepository.save(newWine.getBrandID());
        }

        if (countriesRepository.existsCountriesByCountryName(wine.getCountryID().getCountryName())) {
            newWine.setCountryID(countriesRepository.findCountryByCountryName(wine.getCountryID().getCountryName()));
        } else {
            countriesRepository.save(newWine.getCountryID());
        }

        Grapes grapes;
        if (grapesRepository.existsGrapesByGrapeName(wine.getGrapeType())) {
            grapes = grapesRepository.findGrapeByGrapeName(wine.getGrapeType());
        } else {
            grapes = new Grapes(wine.getGrapeType());
            grapesRepository.save(grapes);
        }
        wineRepository.save(newWine);
        wineGrapesRepository.save(new WineGrapes(newWine, grapes));

        return wine;
    }

    @GetMapping(path = "/all-grapes")
    @ResponseBody
    public String getAllGrapes() {
        Iterable<Grapes> grapes = grapesRepository.findAll();
        String html = "";
        for (Grapes someGrape : grapes) {
            html += someGrape + "<br>";
        }

        return html;
    }

    @GetMapping(path = "/all-brands")
    @ResponseBody
    public String getAllBrands() {
        Iterable<Brands> brands = brandsRepository.findAll();
        String html = "";
        for (Brands someBrand : brands) {
            html += someBrand + "<br>";
        }

        return html;
    }

    @GetMapping(path = "/all-countries")
    @ResponseBody
    public String getAllCountries() {
        Iterable<Countries> countries = countriesRepository.findAll();
        String html = "";
        for (Countries someCountry : countries) {
            html += someCountry + "<br>";
        }
        return html;
    }

    @GetMapping(path = "/all-wines")
    @ResponseBody
    public String getAllWines() {
        Iterable<Wine> wines = wineRepository.findAll();
        String html = "";
        for (Wine someWine : wines) {
            html += someWine + "<br>";
        }
        return html;
    }

    @GetMapping(path = "/all-products")
    @ResponseBody
    public String getAllProducts() {
        UpdateProducts.UpdateProductsMessage message = parserService.getMessage();
        String html = "";
        for (UpdateProducts.Product someProduct : message.getProductsList()) {
            html += "<a>Name: </a>" + someProduct.getName() + "<br>";
            html += "<a>Link: </a>" + someProduct.getLink() + "<br>";
            html += "<a>Brand: </a>" + someProduct.getBrand() + "<br>";
            html += "<a>Country: </a>" + someProduct.getCountry() + "<br>";
            html += "<a>Region: </a>" + someProduct.getRegion(0) + "<br>";
            html += "<a>Year: </a>" + someProduct.getYear() + "<br>";
            html += "<a>Grapes: </a>" + someProduct.getGrapeSort(0) + "<br>";
            html += "<a>Volume: </a>" + someProduct.getCapacity() + "<br>";
            html += "<a>ABV: </a>" + someProduct.getStrength() + "<br>";
            html += "<a>Sugar: </a>" + someProduct.getSugar() + "<br>";
            html += "<a>Color: </a>" + someProduct.getColor() + "<br>";
            html += "<a>New Price: </a>" + someProduct.getNewPrice() + "<br>";
            html += "<a>Old Price: </a>" + someProduct.getOldPrice() + "<br><br>";
        }
        return html;
    }

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