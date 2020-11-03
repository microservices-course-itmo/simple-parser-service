package com.wine.to.up.simple.parser.service.SimpleParser.DbHandler;

import com.wine.to.up.simple.parser.service.SimpleParser.SimpleWine;
import com.wine.to.up.simple.parser.service.domain.entity.*;
import com.wine.to.up.simple.parser.service.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import java.util.LinkedList;
import java.util.List;

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
    private final ModelMapper modelMapper;


    /**  The service instance creation. */
    public WineService(GrapesRepository grapesRepository, BrandsRepository brandsRepository, CountriesRepository countriesRepository, WineGrapesRepository wineGrapesRepository, WineRepository wineRepository) {
        brandsService = new BrandsService(brandsRepository);
        countriesService = new CountriesService(countriesRepository);
        grapesService = new GrapesService(grapesRepository);
        wineGrapesService = new WineGrapesService(wineGrapesRepository);
        this.wineRepository = wineRepository;
        modelMapper = new ModelMapper();
    }

    /** Adding all parsed info to the corresponding DB tables/repositories.)
     * @param newWine {@link SimpleWine} class instance containing all wine parsed info
     */
    public void saveAllWineParsedInfo(SimpleWine newWine) {
        Float price = newWine.getNewPrice();
        String link = newWine.getLink();
        if (Boolean.TRUE.equals(wineRepository.existsWineByLinkAndNewPrice(link, price))) {
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

        List<Grapes> grapesEntity = new LinkedList<>();
        for (String grape : newWine.getGrapeSort()) {
            try {
                grapesEntity.add(grapesService.saveGrape(grape));
            } catch (Exception e) {
                log.error("DB error: problem with saveGrape: ", e);
            }
        }

        if ((brandEntity != null) && (countryEntity != null)) {
            Wine wineEntity = saveWine(newWine, brandEntity, countryEntity);
            for (Grapes grapeEntity : grapesEntity) {
                wineGrapesService.saveWineGrapes(grapeEntity, wineEntity);
            }
        }
    }

    /**
     * Adding wine to DB ({@link WineRepository})
     *
     * @param newWine {@link SimpleWine} class instance containing all wine parsed info
     * @return instance of the {@link Wine} entity
     */
    private Wine saveWine(SimpleWine newWine, Brands brandEntity, Countries countryEntity) {
        Wine wineEntity = modelMapper.map(newWine, Wine.class);
        wineEntity.setCountryID(countryEntity);
        wineEntity.setBrandID(brandEntity);
        wineEntity.setGrapeSort(String.valueOf(newWine.getGrapeSort()));

        wineRepository.save(wineEntity);
        log.trace("New Wine was added to DB: " + wineEntity.toString());
        return wineEntity;
    }
}
