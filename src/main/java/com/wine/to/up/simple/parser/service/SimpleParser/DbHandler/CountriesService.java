package com.wine.to.up.simple.parser.service.SimpleParser.DbHandler;

import com.wine.to.up.simple.parser.service.domain.entity.Countries;
import com.wine.to.up.simple.parser.service.repository.CountriesRepository;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CountriesService {
    private final CountriesRepository countriesRepository;

    public CountriesService(CountriesRepository countriesRepository) {
        this.countriesRepository = countriesRepository;
    }

    protected Countries saveCountry(@NonNull String country) {
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
}
