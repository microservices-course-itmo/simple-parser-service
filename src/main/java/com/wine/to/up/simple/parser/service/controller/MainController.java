package com.wine.to.up.simple.parser.service.controller;

import com.wine.to.up.parser.common.api.schema.ParserApi;
import com.wine.to.up.simple.parser.service.simple_parser.ParserService;
import com.wine.to.up.simple.parser.service.domain.entity.*;
import com.wine.to.up.simple.parser.service.repository.*;
import com.wine.to.up.simple.parser.service.simple_parser.enums.Cities;
import com.wine.to.up.simple.parser.service.simple_parser.mappers.WineMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * REST Controller that processes user requests to parser and to DB
 */
@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping(path = "/parser")
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

    private final WineMapper wineMapper;


    @Autowired
    public MainController(WineRepository wineRepository, ParserService parserService, WineMapper wineMapper) {
        this.wineRepository = wineRepository;
        this.parserService = parserService;
        this.wineMapper = wineMapper;
    }

    /**
     * The method based on a GET request. The parser runs when the request is received. If an incorrect number of pages is entered, the error is written to the logs.
     */
    @PostMapping(path = "/run-parser")
    public void runParser(@RequestParam int pagesToParse, @RequestParam int sparklingPagesToParse, @RequestParam Cities city) {
        parserService.startParser(pagesToParse, sparklingPagesToParse, city.getNumber());
    }

    /**
     * The method based on a GET request. The parser runs on all pages when the request is received.
     */
    @PostMapping(path = "/run-parser-all-pages")
    public void runParserAllPages() {
        parserService.startParser();
    }

    /**
     * The method based on a POST request. Wine data transfer from the database to Kafka.
     */
    @PostMapping(path = "/update")
    public String sendMessageToKafka() {
        Iterable<Wine> wineIterable = wineRepository.findAll();
        List<ParserApi.Wine> wineList = new ArrayList<>();
        for (Wine wine : wineIterable) {
            wineList.add(wineMapper.toKafka(wine).build());
        }
        parserService.generateDividedMessageToKafka(wineList);
        return "Sent " + wineList.size() + " wines to kafka.";
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