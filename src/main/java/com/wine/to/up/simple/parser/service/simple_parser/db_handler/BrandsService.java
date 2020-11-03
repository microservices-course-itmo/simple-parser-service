package com.wine.to.up.simple.parser.service.simple_parser.db_handler;

import com.wine.to.up.simple.parser.service.domain.entity.Brands;
import com.wine.to.up.simple.parser.service.repository.BrandsRepository;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/** The service is responsible for adding a brand to DB.*/
@Service
@Slf4j
public class BrandsService {
    /** The repository that stores brands info (name, id)*/
    private final BrandsRepository brandsRepository;

    /**  The service instance creation.
     * @param brandsRepository not an empty Brands Repository*/
    public BrandsService(@NonNull BrandsRepository brandsRepository) {
        this.brandsRepository = brandsRepository;
    }

    /** Adding to DB with an existence check before
     * @param brand brand name
     * @return instance of the Brands entity
     * @see BrandsRepository
     * */
    protected Brands saveBrand(@NonNull String brand) {
        if (Boolean.FALSE.equals(brandsRepository.existsBrandsByBrandName(brand))) {
            brandsRepository.save(new Brands(brand));
            log.trace("New Brand was added to DB: " + brand);
        }
        return brandsRepository.findBrandByBrandName(brand);
    }
}
