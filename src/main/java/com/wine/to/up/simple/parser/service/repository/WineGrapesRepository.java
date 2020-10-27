package com.wine.to.up.simple.parser.service.repository;


import com.wine.to.up.simple.parser.service.domain.entity.Grapes;
import com.wine.to.up.simple.parser.service.domain.entity.Wine;
import com.wine.to.up.simple.parser.service.domain.entity.WineGrapes;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**The repository that stores the connections between wine and its grapes.*/
@Repository
public interface WineGrapesRepository extends CrudRepository<WineGrapes, UUID> {

    /** Existence check by grape and wine instances
     * @param grape {@link Grapes} entity instance
     * @param wine {@link Wine} entity instance*/
    Boolean existsWineGrapesByGrapeIdAndAndWineId(Grapes grape, Wine wine);
}