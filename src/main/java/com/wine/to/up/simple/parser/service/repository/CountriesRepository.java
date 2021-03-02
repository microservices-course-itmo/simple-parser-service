package com.wine.to.up.simple.parser.service.repository;

import com.wine.to.up.simple.parser.service.domain.entity.Countries;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * The repository that stores countries info ({@link Countries} entities).
 */
@Repository
public interface CountriesRepository extends CrudRepository<Countries, UUID> {

    /**
     * Finding a country by name
     *
     * @param name brand name
     * @return found instance of the {@link Countries} entity.
     */
    Countries findCountryByCountryName(String name);

    /**
     * Existence check by name
     *
     * @param name country name
     */
    Boolean existsCountriesByCountryName(String name);
}