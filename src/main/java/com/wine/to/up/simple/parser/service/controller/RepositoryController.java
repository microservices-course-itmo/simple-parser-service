package com.wine.to.up.simple.parser.service.controller;

import com.wine.to.up.simple.parser.service.dto.BrandsDTO;
import com.wine.to.up.simple.parser.service.dto.CountriesDTO;
import com.wine.to.up.simple.parser.service.dto.GrapesDTO;
import com.wine.to.up.simple.parser.service.dto.WineDTO;
import com.wine.to.up.simple.parser.service.domain.entity.*;
import com.wine.to.up.simple.parser.service.repository.*;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller that processes user requests to DB
 */
@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping(path = "/simple-parser/repository")
public class RepositoryController {

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
     * The repository that stores the connections between wine and its grapes. It's autowired by Spring.
     */
    private WineGrapesRepository wineGrapesRepository;

    /**
     * The repository that stores all info about wine. It's autowired by Spring.
     */
    private WineRepository wineRepository;


    @Autowired
    public RepositoryController(GrapesRepository grapesRepository, BrandsRepository brandsRepository, CountriesRepository countriesRepository, WineGrapesRepository wineGrapesRepository, WineRepository wineRepository) {
        this.grapesRepository = grapesRepository;
        this.brandsRepository = brandsRepository;
        this.countriesRepository = countriesRepository;
        this.wineGrapesRepository = wineGrapesRepository;
        this.wineRepository = wineRepository;
    }

    /**
     * The method based on a POST request. A new grape type is added to the database when the request is received.
     *
     * @param grape Grape type model. All arguments will be taken from this model.
     * @return instance of the Grape entity.
     * @see Grapes
     */
    @PostMapping(path = "/grape")
    @ResponseBody
    public Grapes addGrape(@ModelAttribute("grape") GrapesDTO grape) {
        Grapes newGrape = new Grapes(grape.getGrapeName());
        grapesRepository.save(newGrape);
        return newGrape;
    }

    /**
     * The method based on a POST request. A new brand is added to the database when the request is received.
     *
     * @param brand Brand model. All arguments will be taken from this model.
     * @return instance of the Brand entity.
     * @see Brands
     */
    @PostMapping(path = "/brand")
    @ResponseBody
    public Brands addBrand(@ModelAttribute("brand") BrandsDTO brand) {
        Brands newBrand = new Brands(brand.getBrandName());
        brandsRepository.save(newBrand);
        return newBrand;
    }

    /**
     * The method based on a POST request. A new country is added to the database when the request is received.
     *
     * @param country Country model. All arguments will be taken from this model.
     * @return instance of the Countries entity.
     * @see Countries
     */
    @PostMapping(path = "/country")
    @ResponseBody
    public Countries addCountry(@ModelAttribute("country") CountriesDTO country) {
        Countries newCountry = new Countries(country.getCountryName());
        countriesRepository.save(newCountry);
        return newCountry;
    }

    /**
     * The method based on a POST request. All info about new wine is added to the database when the request is received.
     *
     * @param wine Wine type model. All arguments will be taken from this model.
     * @return instance of the Wine entity.
     * @see Wine
     */
    @PostMapping(path = "/wine")
    @ResponseBody
    public Wine addWine(@ModelAttribute("wine") WineDTO wine) {
        ModelMapper modelMapper = new ModelMapper();
        Wine newWine = modelMapper.map(wine, Wine.class);
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
        String grapeSort = wine.getGrapeSort();
        if (Boolean.TRUE.equals(grapesRepository.existsGrapesByGrapeName(grapeSort))) {
            grapes = grapesRepository.findGrapeByGrapeName(grapeSort);
        } else {
            grapes = new Grapes(grapeSort);
            grapesRepository.save(grapes);
        }
        wineRepository.save(newWine);
        wineGrapesRepository.save(new WineGrapes(newWine, grapes));

        return newWine;
    }
}
