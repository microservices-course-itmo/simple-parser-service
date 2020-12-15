package com.wine.to.up.simple.parser.service.repository;

import com.wine.to.up.simple.parser.service.domain.entity.Grapes;
import com.wine.to.up.simple.parser.service.domain.entity.Wine;
import com.wine.to.up.simple.parser.service.domain.entity.WineGrapes;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * The repository that stores the connections between {@link Wine} and its {@link Grapes}.
 */
@Repository
public interface WineGrapesRepository extends CrudRepository<WineGrapes, UUID> {

    /**
     * Existence check by grape and wine instances
     *
     * @param grape {@link Grapes} entity instance
     * @param wine  {@link Wine} entity instance
     */
    Boolean existsWineGrapesByGrapeIdAndAndWineId(Grapes grape, Wine wine);

    /**
     * Finding a connection between the wine and its grape sort by ID
     *
     * @param grape grape ID
     * @param wine  wine ID
     * @return found instance of the {@link WineGrapes} entity.
     */
    WineGrapes findByGrapeIdAndAndWineId(Grapes grape, Wine wine);
}