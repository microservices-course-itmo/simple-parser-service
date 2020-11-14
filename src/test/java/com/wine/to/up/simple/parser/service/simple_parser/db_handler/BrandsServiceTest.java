package com.wine.to.up.simple.parser.service.simple_parser.db_handler;

import com.wine.to.up.simple.parser.service.domain.entity.Brands;
import com.wine.to.up.simple.parser.service.repository.BrandsRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

/**
 * Class for testing {@link WineService}
 */
@RunWith(MockitoJUnitRunner.class)
public class BrandsServiceTest {
    @InjectMocks
    private BrandsService brandsService;
    @Mock
    private BrandsRepository brandsRepository;
    private final Brands brand = new Brands("vvvino");

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void saveBrandTest() {
        when(brandsRepository.save(brand)).thenReturn(brand);
        Brands newBrand = brandsRepository.save(brand);
        assertEquals(brand, newBrand);
    }

    @Test
    public void findBrandByNameTest() {
        when(brandsRepository.findBrandByBrandName(brand.getBrandName())).thenReturn(brand);
        Brands newBrand = brandsRepository.findBrandByBrandName("vvvino");
        assertEquals(brand, newBrand);
    }

    @Test
    public void existBrandByNameTest() {
        when(brandsRepository.existsBrandsByBrandName(brand.getBrandName())).thenReturn(true);
        assertEquals(true, brandsRepository.existsBrandsByBrandName("vvvino"));
    }

    @Test
    public void findAllBrandsTest() {
        ArrayList<Brands> brands = new ArrayList<>();
        brands.add(brand);
        when(brandsRepository.findAll()).thenReturn(brands);
        assertEquals(brands, brandsRepository.findAll());
    }

    @Test
    public void testSaveBrandService() {
        when(brandsService.saveBrand("vvvino")).thenReturn(brand);
        assertEquals("vvvino", brandsService.saveBrand("vvvino").getBrandName());
    }
}
