package com.wine.to.up.simple.parser.service.repository;

import com.wine.to.up.simple.parser.service.domain.entity.Wine;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * The repository that stores all info about wine (Wine Entities).
 */
@Repository
public interface WineRepository extends CrudRepository<Wine, UUID> {

    /**
     * Finding a wine by name, price, volume, colo, sugar. Full compliance.
     *
     * @return found instance of the Wine entity.
     */
    Wine findWineByNameAndNewPriceAndCapacityAndColorAndSugar(String name, Float price, Float volume, String colorType, String sugarType);

    /**
     * Finding a wine by link and price. One wine has one link.
     *
     * @param link  link to the wine page
     * @param price wine price
     * @return found instance of the Wine entity.
     */
    Wine findWineByLinkAndNewPrice(String link, float price);

    /**
     * Existence check by link and price. One wine has one link.
     *
     * @param link  link to the wine page
     * @param price wine price
     */
    Boolean existsWineByLinkAndNewPrice(String link, float price);

    /**
     * Existence check by name, price, volume, colo, sugar. Full compliance.
     */
    Boolean existsWineByNameAndNewPriceAndCapacityAndColorAndSugarAndYear(String name, Float price, Float volume, String colorType, String sugarType, int year);
}
