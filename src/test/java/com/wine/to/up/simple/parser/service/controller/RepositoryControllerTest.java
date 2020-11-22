package com.wine.to.up.simple.parser.service.controller;

import com.wine.to.up.simple.parser.service.domain.entity.Brands;
import com.wine.to.up.simple.parser.service.domain.entity.Countries;
import com.wine.to.up.simple.parser.service.domain.entity.Wine;
import com.wine.to.up.simple.parser.service.dto.BrandsDTO;
import com.wine.to.up.simple.parser.service.dto.CountriesDTO;
import com.wine.to.up.simple.parser.service.dto.GrapesDTO;
import com.wine.to.up.simple.parser.service.dto.WineDTO;
import com.wine.to.up.simple.parser.service.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;


import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
class RepositoryControllerTest {
    @Mock
    private GrapesRepository grapesRepository;
    @Mock
    private BrandsRepository brandsRepository;
    @Mock
    private CountriesRepository countriesRepository;
    @Mock
    private WineGrapesRepository wineGrapesRepository;
    @Mock
    private WineRepository wineRepository;
    private RepositoryController repositoryController;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
        repositoryController = new RepositoryController(grapesRepository, brandsRepository, countriesRepository, wineGrapesRepository, wineRepository);
    }

    @Test
    void testAddGrape() {
        GrapesDTO grapesDTO = new GrapesDTO("Test grape");
        assertNotNull(repositoryController.addGrape(grapesDTO));
        assertEquals("Test grape", repositoryController.addGrape(grapesDTO).getGrapeName());
    }

    @Test
    void testAddBrand() {
        BrandsDTO brandsDTO = new BrandsDTO("Test brand");
        assertNotNull(repositoryController.addBrand(brandsDTO));
        assertEquals("Test brand", repositoryController.addBrand(brandsDTO).getBrandName());
    }

    @Test
    void testAddCountry() {
        CountriesDTO countriesDTO = new CountriesDTO("Test country");
        assertNotNull(repositoryController.addCountry(countriesDTO));
        assertEquals("Test country", repositoryController.addCountry(countriesDTO).getCountryName());
    }

    @Test
    void testAddWine() {
        WineDTO wineDTO = new WineDTO();
        wineDTO.setName("Test wine");
        wineDTO.setBrandID(new Brands());
        wineDTO.setCapacity((float) 10.0);
        wineDTO.setCountryID(new Countries());
        wineDTO.setNewPrice((float) 10.0);
        Wine res = repositoryController.addWine(wineDTO);
        assertNotNull(res);
        assertEquals("Test wine", res.getName());
    }
}
