package com.wine.to.up.simple.parser.service.simple_parser.db_handler;

import com.wine.to.up.simple.parser.service.domain.entity.Brands;
import com.wine.to.up.simple.parser.service.repository.BrandsRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.Assert.*;

/**
 * Class for testing {@link WineService}
 */
@SpringBootTest
public class BrandsServiceTest {

    @Autowired
    private BrandsRepository brandsRepository;
    @Autowired
    private BrandsService brandsService;

    @Test
    void testSaveBrand() {
        String brandName = "Vivino";
        brandsService.saveBrand(brandName);
        assertEquals(brandName, brandsRepository.findBrandByBrandName(brandName).getBrandName());
    }

    @Test
    void testDoubleSave() {
        String brandName = "Vivino";
        brandsService.saveBrand(brandName);
        brandsService.saveBrand(brandName);
        int counter = 0;
        for (Brands i : brandsRepository.findAll()) {
            if (i.getBrandName().equals(brandName)) {
                counter++;
            }
        }
        assertEquals(1, counter);
    }
}
