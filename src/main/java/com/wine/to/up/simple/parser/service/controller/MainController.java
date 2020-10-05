package com.wine.to.up.simple.parser.service.controller;

import com.wine.to.up.simple.parser.service.domain.entity.*;
import com.wine.to.up.simple.parser.service.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
@Validated
@Slf4j
@RequestMapping(path = "/simpleparser")
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


    @PostMapping(path="/grape")
    @ResponseBody
    public String addGrape(@RequestParam String grapeName){
        grapesRepository.save(new Grapes(grapeName));
        return "New Grape Added";
    }

    @PostMapping(path="/brand")
    @ResponseBody
    public String addBrand(@RequestParam String brandName){
        brandsRepository.save(new Brands(brandName));
        return "New Brand Added";
    }

    @PostMapping(path="/country")
    @ResponseBody
    public String addCountry(@RequestParam String countryName){
        countriesRepository.save(new Countries(countryName));
        return "New Country Added";
    }

    @PostMapping(path="/wine")
    @ResponseBody
    public String addWine(@RequestParam String name, @RequestParam String brandS, @RequestParam String countryS,
                          @RequestParam Float volume, @RequestParam Float abv, @RequestParam String colorType,
                          @RequestParam String sugarType, @RequestParam List<String> wineGrapes){

        Brands brand=brandsRepository.findBrandByBrandName(brandS);
        Countries country=countriesRepository.findCountryByCountryName(countryS);
        Wine newWine = new Wine(name, brand, country, volume, abv, colorType, sugarType);
        wineRepository.save(newWine);

        for(String someGrape: wineGrapes){
            wineGrapesRepository.save(new WineGrapes(newWine, grapesRepository.findGrapeByGrapeName(someGrape)));
        }

        return "New Wine Added";
    }

    @GetMapping(path="/all-grapes")
    @ResponseBody
    public Iterable<Grapes> getAllGrapes() {
        return grapesRepository.findAll();
    }

    @GetMapping(path="/all-brands")
    @ResponseBody
    public Iterable<Brands> getAllBrands() {
        return brandsRepository.findAll();
    }

    @GetMapping(path="/all-countries")
    @ResponseBody
    public Iterable<Countries> getAllCountries() {
        return countriesRepository.findAll();
    }

//    @GetMapping(path="/all-wine-grapes")
//    @ResponseBody
//    public Iterable<WineGrapesInfo> getAllWineGrapesInfo() {
//        return wineGrapesRepository.findAll();
//    }

    @GetMapping(path="/all-wines")
    @ResponseBody
    public Iterable<Wine> getAllWines() {
        return wineRepository.findAll();
    }
}