package com.wine.to.up.simple.parser.service.repository;

import com.wine.to.up.simple.parser.service.domain.entity.Brands;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * The repository that stores brands info ({@link Brands} entities).
 */
@Repository
public interface BrandsRepository extends CrudRepository<Brands, UUID> {

    /**
     * Finding a brand by name
     *
     * @param name brand name
     * @return found instance of the {@link Brands} entity.
     */
    Brands findBrandByBrandName(String name);

    /**
     * Existence check by name
     *
     * @param name brand name
     */
    Boolean existsBrandsByBrandName(String name);
}
