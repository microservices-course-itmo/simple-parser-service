package com.wine.to.up.simple.parser.service.controller;

import com.wine.to.up.simple.parser.service.domain.entity.*;
import com.wine.to.up.simple.parser.service.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.sql.Array;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;





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
    private WineGrapesInfoRepository wineGrapesInfoRepository;
    @Autowired
    private WineRepository wineRepository;


    @PostMapping(path="/addGrape")
    public @ResponseBody String addGrape(@RequestParam String grapeName){
        grapesRepository.save(new Grapes(grapeName));
        return "New Grape Added";
    }

    @PostMapping(path="/addBrand")
    public @ResponseBody String addBrand(@RequestParam String brandName){
        brandsRepository.save(new Brands(brandName));
        return "New Brand Added";
    }

    @PostMapping(path="/addCountry")
    public @ResponseBody String addCountry(@RequestParam String countryName){
        countriesRepository.save(new Countries(countryName));
        return "New Country Added";
    }



    @PostMapping(path="/addWineGraspe")
    public @ResponseBody String addWineGrape(@RequestParam List<String> grapeNames) {

        WineGrapesInfo wineGrapesInfo = new WineGrapesInfo();
        UUID wineGrapeID = wineGrapesInfo.getWineGrapesID();
        Iterable<Grapes> grapesList = getAllGrapes();


        for (String grape : grapeNames) {
            for (Grapes iGrape : grapesList) {
                if (iGrape.getGrapeName().equals(grape)) {
                    wineGrapesInfo.setWineGrapesID(wineGrapeID);
                    wineGrapesInfo.setGrapeID(iGrape);
                    wineGrapesInfoRepository.save(wineGrapesInfo);
                    wineGrapesInfo = new WineGrapesInfo();
                }
            }
        }
        return "New Wine Grape Added";
    }

    @PostMapping(path="/addWine")
    public @ResponseBody String addWine(@RequestParam String brandS, @RequestParam String countryS,
                                        @RequestParam Float volume, @RequestParam Float abv, @RequestParam String colorType,
                                        @RequestParam String sugarType){

        Brands brand=null;
        Iterable<Brands> brandList = getAllBrands();
        for(Brands iBrand: brandList){
            if(iBrand.getBrandName().equals(brandS))
                brand = iBrand;
        }

        Countries country=null;
        Iterable<Countries> countryList = getAllCountries();
        for(Countries iCountry: countryList){
            if(iCountry.getCountryName().equals(countryS))
                country = iCountry;
        }

//        ArrayList<WineGrapesInfo> wineGrapesInfo = new ArrayList<>();
//        Iterable<WineGrapesInfo> wineGrapesInfoList = getAllWineGrapesInfo();
//        for(WineGrapesInfo iWineGrapeInfo: wineGrapesInfoList){
//            if(iWineGrapeInfo.getWineGrapesID().equals(wineGrapesInfoID))
//                wineGrapesInfo.add(iWineGrapeInfo);
//        }

        wineRepository.save(new Wine(brand, country, volume, abv, colorType, sugarType));
        return "New Wine Added";
    }

    @GetMapping(path="/allGrapes")
    public @ResponseBody Iterable<Grapes> getAllGrapes() {
        return grapesRepository.findAll();
    }

    @GetMapping(path="/allBrands")
    public @ResponseBody Iterable<Brands> getAllBrands() {
        return brandsRepository.findAll();
    }

    @GetMapping(path="/allCountries")
    public @ResponseBody Iterable<Countries> getAllCountries() {
        return countriesRepository.findAll();
    }

    @GetMapping(path="/allWineGrapesInfo")
    public @ResponseBody Iterable<WineGrapesInfo> getAllWineGrapesInfo() {
        return wineGrapesInfoRepository.findAll();
    }

    @GetMapping(path="/allWine")
    public @ResponseBody Iterable<Wine> getAllWine() {
        return wineRepository.findAll();
    }


}
