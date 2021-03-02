package com.wine.to.up.simple.parser.service.simple_parser.db_handler;

import com.wine.to.up.simple.parser.service.simple_parser.SimpleWine;
import com.wine.to.up.simple.parser.service.domain.entity.*;
import com.wine.to.up.simple.parser.service.repository.*;
import com.wine.to.up.simple.parser.service.simple_parser.mappers.WineMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

/**
 * The service is responsible for adding a {@link Wine} entity to DB.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class WineService {
    /**
     * The repository that stores all info about wine.
     */
    private final WineRepository wineRepository;
    private final BrandsService brandsService;
    private final CountriesService countriesService;
    private final GrapesService grapesService;
    private final WineGrapesService wineGrapesService;
    private final WineMapper wineMapper;

    /**
     * Adding all parsed info to the corresponding DB tables/repositories.)
     *
     * @param newWine {@link SimpleWine} class instance containing all wine parsed info
     */
    public Wine saveAllWineParsedInfo(SimpleWine newWine) {
        Float price = newWine.getNewPrice();
        String link = newWine.getLink();
        if (Boolean.TRUE.equals(wineRepository.existsWineByLinkAndNewPrice(link, price))) {
            return wineRepository.findWineByLinkAndNewPrice(link, price);
        }
        String brand = newWine.getBrand();
        Brands brandEntity = null;
        try {
            brandEntity = brandsService.saveBrand(brand);
            newWine.setBrandID(brandEntity);
        } catch (Exception e) {
            log.error("DB error: problem with saveBrand: ", e);
        }

        String country = newWine.getCountry();
        Countries countryEntity = null;
        try {
            countryEntity = countriesService.saveCountry(country);
            newWine.setCountryID(countryEntity);
        } catch (Exception e) {
            log.error("DB error: problem with saveCountry: ", e);
        }

        List<Grapes> grapesEntity = null;
        if (newWine.getGrapeSort() != null) {
            grapesEntity = new LinkedList<>();
            for (String grape : newWine.getGrapeSort()) {
                try {
                    grapesEntity.add(grapesService.saveGrape(grape));
                } catch (Exception e) {
                    log.error("DB error: problem with saveGrape: ", e);
                }
            }
        }

        if ((brandEntity != null) && (countryEntity != null) && (grapesEntity != null)) {
            Wine wineEntity = saveWine(newWine);
            for (Grapes grapeEntity : grapesEntity) {
                wineGrapesService.saveWineGrapes(grapeEntity, wineEntity);
            }
            return wineEntity;
        }
        return null;
    }

    /**
     * Adding wine to DB ({@link WineRepository})
     *
     * @param newWine {@link SimpleWine} class instance containing all wine parsed info
     * @return instance of the {@link Wine} entity
     */
    private Wine saveWine(SimpleWine newWine) {
        Wine wineEntity = wineMapper.toEntity(newWine);
        wineRepository.save(wineEntity);
        log.trace("New Wine was added to DB: {}", wineEntity.toString());

        return wineEntity;
    }
}
