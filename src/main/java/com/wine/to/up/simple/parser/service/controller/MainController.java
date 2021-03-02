package com.wine.to.up.simple.parser.service.controller;

import com.wine.to.up.simple.parser.service.simple_parser.ParserService;
import com.wine.to.up.simple.parser.service.domain.entity.*;
import com.wine.to.up.simple.parser.service.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller that processes user requests to parser and to DB
 */
@RestController
@RequiredArgsConstructor
@Validated
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
        if (pagesToParse > 0 || sparklingPagesToParse > 0) {
            parserService.startParser(pagesToParse, sparklingPagesToParse);
            return "Parser started by request";
        } else {
            return "Parser didn't start";
        }
    }

    /**
     * The method based on a GET request. The parser runs on all pages when the request is received.
     *
     * @return message about successful execution.
     */
    @GetMapping(path = "/run-parser-all-pages")
    public String runParserAllPages() {
        parserService.startParser();
        return "Parser started by request";
    }

    /**
     * The method based on a GET request. Output of all grape types stored in the grapesRepository {@link GrapesRepository}.
     *
     * @return list of all {@link Grapes}
     */
    @GetMapping(path = "/all-grapes")
    @ResponseBody
    public Iterable<Grapes> getAllGrapes() {
        return grapesRepository.findAll();
    }

    /**
     * The method based on a GET request. Output of all brands stored in the {@link BrandsRepository}.
     *
     * @return list of all {@link Brands}
     */
    @GetMapping(path = "/all-brands")
    @ResponseBody
    public Iterable<Brands> getAllBrands() {
        return brandsRepository.findAll();
    }

    /**
     * The method based on a GET request. Output of all countries stored in the {@link CountriesRepository}.
     *
     * @return list of {@link Countries}
     */
    @GetMapping(path = "/all-countries")
    @ResponseBody
    public Iterable<Countries> getAllCountries() {
        return countriesRepository.findAll();
    }

    /**
     * A method based on a GET request. Output of all wines stored in the {@link WineRepository}.
     *
     * @return list of all wines as iterable list
     */
    @GetMapping(path = "/all-wines")
    @ResponseBody
    public Iterable<Wine> getAllWines() {
        return wineRepository.findAll();
    }

    /**
     * A method based on a GET request. Output of a wine stored in the {@link WineRepository} by name.
     *
     * @return instance list of {@link Wine}s
     */
    @GetMapping(path = "/wine-by-name")
    @ResponseBody
    public Iterable<Wine> getWineByName(@RequestParam String name) {
        return wineRepository.findWineByName(name);
    }
}