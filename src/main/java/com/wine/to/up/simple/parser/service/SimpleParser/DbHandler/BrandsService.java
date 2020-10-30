package com.wine.to.up.simple.parser.service.SimpleParser.DbHandler;

import com.wine.to.up.simple.parser.service.domain.entity.Brands;
import com.wine.to.up.simple.parser.service.repository.BrandsRepository;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class BrandsService {
    private final BrandsRepository brandsRepository;

    public BrandsService(@NonNull BrandsRepository brandsRepository) {
        this.brandsRepository = brandsRepository;
    }

    public Brands saveBrand(@NonNull String brand) {
        if (!brandsRepository.existsBrandsByBrandName(brand)) {
            brandsRepository.save(new Brands(brand));
            log.trace("New Brand was added to DB: " + brand);
        }

        return brandsRepository.findBrandByBrandName(brand);
    }
}
