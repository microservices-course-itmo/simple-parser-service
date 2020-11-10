package com.wine.to.up.simple.parser.service.simple_parser.db_handler;

import com.wine.to.up.simple.parser.service.domain.entity.Countries;
import com.wine.to.up.simple.parser.service.repository.CountriesRepository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.Assert.*;

/**
 * Class for testing {@link CountriesService.java}
 */
@SpringBootTest
public class CountriesServiceTest {
    @Autowired
    private CountriesRepository countriesRepository;
    @Autowired
    private CountriesService countriesService;

    @Test
    void testSaveCountry() {
        String countryName = "Russia";
        countriesService.saveCountry(countryName);
        assertEquals(countryName, countriesRepository.findCountryByCountryName(countryName).getCountryName());
    }

    @Test
    void testDoubleSave() {
        String countryName = "Russia";
        countriesService.saveCountry(countryName);
        countriesService.saveCountry(countryName);
        int counter = 0;
        for (Countries i : countriesRepository.findAll()) {
            if (i.getCountryName().equals(countryName)) {
                counter++;
            }
        }
        assertEquals(1, counter);
    }

}
