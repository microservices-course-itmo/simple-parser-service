package com.wine.to.up.simple.parser.service.simple_parser.db_handler;


import com.wine.to.up.simple.parser.service.domain.entity.Countries;
import com.wine.to.up.simple.parser.service.repository.CountriesRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

/**
 * Class for testing {@link CountriesService}
 */
@RunWith(MockitoJUnitRunner.class)
public class CountriesServiceTest {
    private final Countries country = new Countries("Раися");
    @InjectMocks
    private CountriesService countriesService;
    @Mock
    private CountriesRepository countriesRepository;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void saveCountryTest() {
        when(countriesRepository.save(country)).thenReturn(country);
        Countries newCountry = countriesRepository.save(country);
        assertEquals(country, newCountry);
    }

    @Test
    public void findBrandByNameTest() {
        when(countriesRepository.findCountryByCountryName(country.getCountryName())).thenReturn(country);
        Countries newCountry = countriesRepository.findCountryByCountryName(country.getCountryName());
        assertEquals(country, newCountry);
    }

    @Test
    public void existBrandByNameTest() {
        when(countriesRepository.existsCountriesByCountryName(country.getCountryName())).thenReturn(true);
        assertEquals(true, countriesRepository.existsCountriesByCountryName(country.getCountryName()));
    }

    @Test
    public void findAllCountriesTest() {
        ArrayList<Countries> countries = new ArrayList<>();
        countries.add(country);
        when(countriesRepository.findAll()).thenReturn(countries);
        assertEquals(countries, countriesRepository.findAll());
    }

    @Test
    public void testSaveCountryService() {
        when(countriesService.saveCountry("Раися")).thenReturn(country);
        assertEquals("Раися", countriesService.saveCountry("Раися").getCountryName());
    }
}
