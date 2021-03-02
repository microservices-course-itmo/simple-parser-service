package com.wine.to.up.simple.parser.service.simple_parser.db_handler;

import com.wine.to.up.simple.parser.service.domain.entity.Countries;
import com.wine.to.up.simple.parser.service.repository.CountriesRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * The service is responsible for adding a {@link Countries} entity to DB.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class CountriesService {
    /**
     * The repository that stores countries info (name, id)
     */
    private final CountriesRepository countriesRepository;

    /**
     * Adding to DB with an existence check before
     *
     * @param country country name
     * @return instance of the {@link Countries} entity
     * @see CountriesRepository
     */
    protected Countries saveCountry(@NonNull String country) {
        if (Boolean.FALSE.equals(countriesRepository.existsCountriesByCountryName(country))) {
            countriesRepository.save(new Countries(country));
            log.trace("New Brand was added to DB: {}", country);
        }
        return countriesRepository.findCountryByCountryName(country);
    }
}
