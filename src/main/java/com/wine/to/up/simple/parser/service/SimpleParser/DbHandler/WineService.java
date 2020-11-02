package com.wine.to.up.simple.parser.service.SimpleParser.DbHandler;

import com.wine.to.up.simple.parser.service.SimpleParser.SimpleWine;
import com.wine.to.up.simple.parser.service.domain.entity.*;
import com.wine.to.up.simple.parser.service.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.UUID;

/** The service is responsible for adding all info to DB.*/
@Service
@Slf4j
public class WineService {
    /** The repository that stores all info about wine. */
    private final WineRepository wineRepository;
    private final BrandsService brandsService;
    private final CountriesService countriesService;
    private final GrapesService grapesService;
    private final WineGrapesService wineGrapesService;


    /**  The service instance creation. */
    public WineService(GrapesRepository grapesRepository, BrandsRepository brandsRepository, CountriesRepository countriesRepository, WineGrapesRepository wineGrapesRepository, WineRepository wineRepository) {
        brandsService = new BrandsService(brandsRepository);
        countriesService = new CountriesService(countriesRepository);
        grapesService = new GrapesService(grapesRepository);
        wineGrapesService = new WineGrapesService(wineGrapesRepository);
        this.wineRepository = wineRepository;
    }

    /** Adding all parsed info to the corresponding DB tables/repositories.)
     * @param newWine {@link SimpleWine} class instance containing all wine parsed info
     * */
    public void saveAllWineParsedInfo(SimpleWine newWine) {
        Float price = newWine.getPrice();
        String link = newWine.getLink();
        if (wineRepository.existsWineByLinkAndPrice(link, price)) {
            return;
        }
        String brand = newWine.getBrandID();
        Brands brandEntity = null;
        try {
            brandEntity = brandsService.saveBrand(brand);
        } catch (Exception e) {
            log.error("DB error: problem with saveBrand: ", e);
        }

        String country = newWine.getCountryID();
        Countries countryEntity = null;
        try {
            countryEntity = countriesService.saveCountry(country);
        } catch (Exception e) {
            log.error("DB error: problem with saveCountry: ", e);
        }

        String grape = newWine.getGrapeType();
        Grapes grapeEntity = null;
        try {
            grapeEntity = grapesService.saveGrape(grape);
        } catch (Exception e) {
            log.error("DB error: problem with saveGrape: ", e);
        }

        if (!(brandEntity == null) && !(countryEntity == null) && !(grapeEntity == null)) {
            Wine wineEntity = saveWine(newWine, brandEntity, countryEntity);
            wineGrapesService.saveWineGrapes(grapeEntity, wineEntity);
        }
    }

    /** Adding wine to DB ({@link WineRepository})
     * @param newWine {@link SimpleWine} class instance containing all wine parsed info
     * @return instance of the {@link Wine} entity
     * */
    private Wine saveWine(SimpleWine newWine, Brands brandEntity, Countries countryEntity) {
        Wine wineEntity = Wine.builder()
                .wineID(UUID.randomUUID())
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
