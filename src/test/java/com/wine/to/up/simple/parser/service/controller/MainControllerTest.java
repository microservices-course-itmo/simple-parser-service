package com.wine.to.up.simple.parser.service.controller;

import com.wine.to.up.simple.parser.service.domain.entity.Brands;
import com.wine.to.up.simple.parser.service.domain.entity.Countries;
import com.wine.to.up.simple.parser.service.domain.entity.Grapes;
import com.wine.to.up.simple.parser.service.domain.entity.Wine;
import com.wine.to.up.simple.parser.service.repository.BrandsRepository;
import com.wine.to.up.simple.parser.service.repository.CountriesRepository;
import com.wine.to.up.simple.parser.service.repository.GrapesRepository;
import com.wine.to.up.simple.parser.service.repository.WineRepository;
import com.wine.to.up.simple.parser.service.simple_parser.ParserService;
import com.wine.to.up.simple.parser.service.simple_parser.enums.City;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.*;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MainControllerTest {
    @Mock
    private GrapesRepository grapesRepository;
    @Mock
    private BrandsRepository brandsRepository;
    @Mock
    private CountriesRepository countriesRepository;
    @Mock
    private WineRepository wineRepository;
    @Mock
    private ParserService parserService;
    @InjectMocks
    private MainController mainController;

    @BeforeEach
    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @ParameterizedTest
    @CsvSource({"1, 1", "0, 1", "1, 0", "-1, -1", "0, 0", "1000, 1000"})
    public void testRunParser(int pagesToParse, int sparklingPagesToParse) {
        Assertions.assertDoesNotThrow(() -> mainController.runParser(pagesToParse, sparklingPagesToParse, City.MOSCOW));
        Mockito.verify(parserService, Mockito.atLeastOnce()).startParser(pagesToParse, sparklingPagesToParse, 1);
    }

    @Test
    public void testRunParserAllPages() {
        Assertions.assertDoesNotThrow(() -> mainController.runParserAllPages());
        Mockito.verify(parserService, Mockito.atLeastOnce()).startParser();
    }

    @Test
    public void testGetAllGrapes() {
        List<Grapes> grapes = new ArrayList<>();
        for (int i = 1; i < 4; i++) {
            Grapes grape = new Grapes();
            grape.setGrapeID(UUID.fromString("5211e915-c3e2-4dcb-0776-c7b900f38ab7"));
            grape.setGrapeName("Grape " + i);
            grapes.add(grape);
        }
        when(grapesRepository.findAll()).thenReturn(grapes);
        Assertions.assertEquals(grapes, mainController.getAllGrapes());
    }

    @Test
    public void testGetAllBrands() {
        List<Brands> brands = new ArrayList<>();
        for (int i = 1; i < 4; i++) {
            Brands brand = new Brands();
            brand.setBrandID(UUID.fromString("5211e915-c3e2-4dcb-0776-c7b900f38ab7"));
            brand.setBrandName("Brand " + i);
            brands.add(brand);
        }
        when(brandsRepository.findAll()).thenReturn(brands);
        Assertions.assertEquals(brands, mainController.getAllBrands());
    }

    @Test
    public void testGetAllCountries() {
        List<Countries> countries = new ArrayList<>();
        for (int i = 1; i < 4; i++) {
            Countries country = new Countries();
            country.setCountryID(UUID.fromString("5211e915-c3e2-4dcb-0776-c7b900f38ab7"));
            country.setCountryName("Country " + i);
            countries.add(country);
        }
        when(countriesRepository.findAll()).thenReturn(countries);
        Assertions.assertEquals(countries, mainController.getAllCountries());
    }

    @Test
    public void testGetAllWines() {
        List<Wine> wines = new ArrayList<>();
        for (int i = 1; i < 4; i++) {
            Wine wine = new Wine();
            wine.setWineID(UUID.fromString("5211e915-c3e2-4dcb-0776-c7b900f38ab7"));
            wine.setName("Wine " + i);
            wines.add(wine);
        }
        when(wineRepository.findAll()).thenReturn(wines);
        Assertions.assertEquals(wines, mainController.getAllWines());
    }

    @Test
    public void testGetWinesByName() {
        List<Wine> wines = new ArrayList<>();
        for (int i = 1; i < 4; i++) {
            Wine wine = new Wine();
            wine.setWineID(UUID.fromString("5211e915-c3e2-4dcb-0776-c7b900f38ab7"));
            wine.setName("Wine");
            wines.add(wine);
        }
        when(wineRepository.findWineByName("Wine")).thenReturn(wines);
        Assertions.assertEquals(wines, mainController.getWineByName("Wine"));
    }
}
