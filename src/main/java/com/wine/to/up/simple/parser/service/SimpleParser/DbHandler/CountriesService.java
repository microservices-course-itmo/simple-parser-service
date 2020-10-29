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

    public CountriesService(@NonNull CountriesRepository countriesRepository) {
        this.countriesRepository = countriesRepository;
    }

    protected Countries saveCountry(@NonNull String country) {
        if (!countriesRepository.existsCountriesByCountryName(country)) {
            countriesRepository.save(new Countries(country));
            log.trace("New Brand was added to DB: " + country);
        }

        return countriesRepository.findCountryByCountryName(country);
    }
}
