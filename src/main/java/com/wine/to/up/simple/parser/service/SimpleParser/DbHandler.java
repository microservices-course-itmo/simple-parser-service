package com.wine.to.up.simple.parser.service.SimpleParser;

import com.wine.to.up.simple.parser.service.domain.entity.*;
import com.wine.to.up.simple.parser.service.repository.*;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class DbHandler {

    private final GrapesRepository grapesRepository;
    private final BrandsRepository brandsRepository;
    private final CountriesRepository countriesRepository;
    private final WineGrapesRepository wineGrapesRepository;
    private final WineRepository wineRepository;

    public DbHandler(GrapesRepository grapesRepository, BrandsRepository brandsRepository, CountriesRepository countriesRepository, WineGrapesRepository wineGrapesRepository, WineRepository wineRepository) {
        this.grapesRepository = grapesRepository;
        this.brandsRepository = brandsRepository;
        this.countriesRepository = countriesRepository;
        this.wineGrapesRepository = wineGrapesRepository;
        this.wineRepository = wineRepository;
    }

    public void putInfoToDB(@NonNull SimpleWine newWine) {
        // Integer.parseInt(newWine.bottleDiscount.replace("-","").replace("%", ""));
        String country = newWine.getCountryID();
        Countries countryEntity = putCountryToDB(country);

        String brand = newWine.getBrandID();
        Brands brandEntity = putBrandToDB(brand);

        String grape = newWine.getGrapeType();
        Grapes grapeEntity = putGrapeToDB(grape);


        Wine wineEntity = putWineToDB(newWine, brandEntity, countryEntity);
        putWineGrapeToDB(grapeEntity, wineEntity);
    }

    private Countries putCountryToDB(@NonNull String country) {
        Countries countryEntity;
        if (countriesRepository.existsCountriesByCountryName(country)) {
            countryEntity = countriesRepository.findCountryByCountryName(country);
            return countryEntity;
        }
        countryEntity = new Countries(country);
        countriesRepository.save(countryEntity);
        log.trace("New Country was added to DB: " + country);
        return countryEntity;
    }

    private Brands putBrandToDB(@NonNull String brand) {
        Brands brandEntity;
        if (brandsRepository.existsBrandsByBrandName(brand)) {
            brandEntity = brandsRepository.findBrandByBrandName(brand);
            return brandEntity;
        }
        brandEntity = new Brands(brand);
        brandsRepository.save(brandEntity);
        log.trace("New Brand was added to DB: " + brand);
        return brandEntity;
    }

    private Grapes putGrapeToDB(@NonNull String grape) {
        Grapes grapeEntity;
        if (grapesRepository.existsGrapesByGrapeName(grape)) {
            grapeEntity = grapesRepository.findGrapeByGrapeName(grape);
            return grapeEntity;
        }
        grapeEntity = new Grapes(grape);
        grapesRepository.save(grapeEntity);
        log.trace("New Grape was added to DB: " + grape);
        return grapeEntity;
    }

    private Wine putWineToDB(@NonNull SimpleWine newWine, @NonNull Brands brandEntity, @NonNull Countries countryEntity) {
        Wine wineEntity;
        String name = newWine.getName();
        Float price = newWine.getPrice();
        Float volume = newWine.getVolume();
        String colorType = newWine.getColorType();
        String sugarType = newWine.getSugarType();
        if (wineRepository.existsWineByNameAndPriceAndVolumeAndColorTypeAndSugarType(name, price,
                volume, colorType, sugarType)) {
            wineEntity = wineRepository.findWineByNameAndPriceAndVolumeAndColorTypeAndSugarType(name, price, volume, colorType, sugarType);
            return wineEntity;
        }
        wineEntity = new Wine(name, brandEntity, countryEntity, price, newWine.getDiscount(),
                volume, newWine.getAbv(), newWine.getYear(), colorType, sugarType, newWine.getGrapeType());
        wineRepository.save(wineEntity);
        log.trace("New Wine was added to DB: " + wineEntity.toString());
        return wineEntity;
    }

    private void putWineGrapeToDB(@NonNull Grapes grapeEntity, @NonNull Wine wineEntity) {
        WineGrapes wineGrapeEntity;
        if (!wineGrapesRepository.existsWineGrapesByGrapeIdAndAndWineId(grapeEntity, wineEntity)) {
            wineGrapeEntity = new WineGrapes(wineEntity, grapeEntity);
            wineGrapesRepository.save(wineGrapeEntity);
            log.trace("New Connection between Wine and Grape was added to DB");
        }
    }

}
