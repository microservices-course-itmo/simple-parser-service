package com.wine.to.up.simple.parser.service.SimpleParser.DbHandler;

import com.wine.to.up.simple.parser.service.domain.entity.Countries;
import com.wine.to.up.simple.parser.service.repository.CountriesRepository;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/** The service is responsible for adding a country to DB.*/
@Service
@Slf4j
public class CountriesService {
    /** The repository that stores countries info (name, id)*/
    private final CountriesRepository countriesRepository;

    /**  The service instance creation. */
    public CountriesService(@NonNull CountriesRepository countriesRepository) {
        this.countriesRepository = countriesRepository;
    }

    /** Adding to DB with an existence check before
     * @param country country name
     * @return instance of the {@link Countries} entity
     * @see CountriesRepository
     * */
    protected Countries saveCountry(@NonNull String country) {
        if (!countriesRepository.existsCountriesByCountryName(country)) {
            countriesRepository.save(new Countries(country));
            log.trace("New Brand was added to DB: " + country);
        }
        return countriesRepository.findCountryByCountryName(country);
    }
}
