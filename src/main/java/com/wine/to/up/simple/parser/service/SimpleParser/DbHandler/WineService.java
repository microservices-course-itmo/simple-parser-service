package com.wine.to.up.simple.parser.service.SimpleParser.DbHandler;

import com.wine.to.up.simple.parser.service.SimpleParser.SimpleWine;
import com.wine.to.up.simple.parser.service.domain.entity.*;
import com.wine.to.up.simple.parser.service.repository.*;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class WineService {

    private final WineRepository wineRepository;
    private static BrandsService brandsService;
    private static CountriesService countriesService;
    private static GrapesService grapesService;
    private static WineGrapesService wineGrapesService;

    public WineService(GrapesRepository grapesRepository, BrandsRepository brandsRepository, CountriesRepository countriesRepository, WineGrapesRepository wineGrapesRepository, WineRepository wineRepository) {
        brandsService = new BrandsService(brandsRepository);
        countriesService = new CountriesService(countriesRepository);
        grapesService = new GrapesService(grapesRepository);
        wineGrapesService = new WineGrapesService(wineGrapesRepository);
        this.wineRepository = wineRepository;
    }

    public void saveAllWineParsedInfo(SimpleWine newWine) {
        String brand = newWine.getBrandID();
        Brands brandEntity = brandsService.saveBrand(brand);

        String country = newWine.getCountryID();
        Countries countryEntity = countriesService.saveCountry(country);

        String grape = newWine.getGrapeType();
        Grapes grapeEntity = grapesService.saveGrape(grape);

        Wine wineEntity = saveWine(newWine, brandEntity, countryEntity);
        wineGrapesService.saveWineGrapes(grapeEntity, wineEntity);
    }

    private Wine saveWine(@NonNull SimpleWine newWine, @NonNull Brands brandEntity, @NonNull Countries countryEntity) {
        Wine wineEntity;
        String name = newWine.getName();
        Float price = newWine.getPrice();
        String link = newWine.getLink();
        String picture = newWine.getPicture();
        int year = newWine.getYear();
        if (wineRepository.existsWineByLinkAndPriceAndPicture(link, price, picture)) {
            wineEntity = wineRepository.findWineByLinkAndPriceAndPicture(link, price, picture);
            return wineEntity;
        }
        wineEntity = new Wine();
        wineEntity = Wine.builder()
                .wineID(wineEntity.getWineID())
                .name(name)
                .price(price)
                .volume(newWine.getVolume())
                .colorType(newWine.getColorType())
                .sugarType(newWine.getSugarType())
                .picture(picture)
                .link(newWine.getLink())
                .brandID(brandEntity)
                .countryID(countryEntity)
                .rating(newWine.getRating())
                .grapeType(newWine.getGrapeType())
                .abv(newWine.getAbv())
                .year(year)
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
