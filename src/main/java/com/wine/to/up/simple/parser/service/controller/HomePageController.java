package com.wine.to.up.simple.parser.service.controller;

import com.wine.to.up.simple.parser.service.simple_parser.ParserService;
import com.wine.to.up.simple.parser.service.domain.entity.*;
import com.wine.to.up.simple.parser.service.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller that processes user requests to home page. This controller displays all information in a convenient form.
 */
@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping(path = "/simple-parser/home-page")
public class HomePageController {

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
    private static final String NEW_LINE = "<br><br>";

    @Autowired
    public HomePageController(GrapesRepository grapesRepository, BrandsRepository brandsRepository, CountriesRepository countriesRepository, WineRepository wineRepository, ParserService parserService) {
        this.grapesRepository = grapesRepository;
        this.brandsRepository = brandsRepository;
        this.countriesRepository = countriesRepository;
        this.wineRepository = wineRepository;
    }

    /**
     * The method based on a GET request. Output of all grape types stored in the grapesRepository {@link GrapesRepository} like a HTML page.
     *
     * @return list of all grape types as HTML
     */
    @GetMapping(path = "/all-grapes-page")
    @ResponseBody
    public String getAllGrapesHTML() {
        Iterable<Grapes> grapes = grapesRepository.findAll();
        StringBuilder html = new StringBuilder();
        for (Grapes someGrape : grapes) {
            html.append(someGrape).append(NEW_LINE);
        }
        return html.toString();
    }

    /**
     * The method based on a GET request. Output of all brands stored in the {@link BrandsRepository} like a HTML page.
     *
     * @return list of all brands as HTML
     */
    @GetMapping(path = "/all-brands-page")
    @ResponseBody
    public String getAllBrandsHTML() {
        Iterable<Brands> brands = brandsRepository.findAll();
        StringBuilder html = new StringBuilder();
        for (Brands someBrand : brands) {
            html.append(someBrand).append(NEW_LINE);
        }
        return html.toString();
    }

    /**
     * The method based on a GET request. Output of all countries stored in the {@link CountriesRepository} like a HTML page.
     *
     * @return list of all countries as HTML
     */
    @GetMapping(path = "/all-countries-page")
    @ResponseBody
    public String getAllCountries() {
        Iterable<Countries> countries = countriesRepository.findAll();
        StringBuilder html = new StringBuilder();
        for (Countries someCountry : countries) {
            html.append(someCountry).append(NEW_LINE);
        }
        return html.toString();
    }

    /**
     * A method based on a GET request. Output of all wines stored in the {@link WineRepository} like a HTML page.
     *
     * @return list of all wines as HTML
     */
    @GetMapping(path = "/all-wines-page")
    @ResponseBody
    public String getAllWinesHTML() {
        Iterable<Wine> wines = wineRepository.findAll();
        StringBuilder html = new StringBuilder();
        for (Wine someWine : wines) {
            html.append(someWine).append(NEW_LINE);
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
        html += " <li><a href='/simple-parser/home-page/all-wines-page'>Show All Wines</a></li>";
        html += " <li><a href='/simple-parser/home-page/all-countries-page'>Show All Countries</a></li>";
        html += " <li><a href='/simple-parser/home-page/all-brands-page'>Show All Brands</a></li>";
        html += " <li><a href='/simple-parser/home-page/all-grapes-page'>Show All Grapes</a></li>";
        html += "</ul>";
        return html;
    }
}