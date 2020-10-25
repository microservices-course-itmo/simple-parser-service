package com.wine.to.up.simple.parser.service.SimpleParser.DbHandler;

import com.wine.to.up.simple.parser.service.SimpleParser.SimpleWine;
import com.wine.to.up.simple.parser.service.domain.entity.*;
import com.wine.to.up.simple.parser.service.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class WineService {
    private final WineRepository wineRepository;
    private final BrandsService brandsService;
    private final CountriesService countriesService;
    private final GrapesService grapesService;
    private final WineGrapesService wineGrapesService;

    @Autowired
    public WineService(GrapesRepository grapesRepository, BrandsRepository brandsRepository, CountriesRepository countriesRepository, WineGrapesRepository wineGrapesRepository, WineRepository wineRepository) {
        brandsService = new BrandsService(brandsRepository);
        countriesService = new CountriesService(countriesRepository);
        grapesService = new GrapesService(grapesRepository);
        wineGrapesService = new WineGrapesService(wineGrapesRepository);
        this.wineRepository = wineRepository;
    }

    public void saveAllWineParsedInfo(SimpleWine newWine) {
        Float price = newWine.getPrice();
        String link = newWine.getLink();
        if (wineRepository.existsWineByLinkAndPrice(link, price)) {
            return;
        }
        String brand = newWine.getBrandID();
        Brands brandEntity = brandsService.saveBrand(brand);

        String country = newWine.getCountryID();
        Countries countryEntity = countriesService.saveCountry(country);

        String grape = newWine.getGrapeType();
        Grapes grapeEntity = grapesService.saveGrape(grape);

        Wine wineEntity = saveWine(newWine, brandEntity, countryEntity);
        wineGrapesService.saveWineGrapes(grapeEntity, wineEntity);
    }

    private Wine saveWine(SimpleWine newWine, Brands brandEntity, Countries countryEntity) {
        Wine wineEntity;
        wineEntity = new Wine();
        wineEntity = Wine.builder()
                .wineID(wineEntity.getWineID())
                .name(newWine.getName())
                .price(newWine.getPrice())
                .volume(newWine.getVolume())
                .colorType(newWine.getColorType())
                .sugarType(newWine.getSugarType())
                .picture(newWine.getPicture())
                .link(newWine.getLink())
                .brandID(brandEntity)
                .countryID(countryEntity)
                .rating(newWine.getRating())
                .grapeType(newWine.getGrapeType())
                .abv(newWine.getAbv())
                .year(newWine.getYear())
                .discount(newWine.getDiscount())
                .region(newWine.getRegion())
                .gastronomy(newWine.getGastronomy())
                .sparkling(newWine.isSparkling())
                .taste(newWine.getTaste())
                .build();
        wineRepository.save(wineEntity);
        log.trace("New Wine was added to DB: " + wineEntity.toString());
        return wineEntity;
    }
}
