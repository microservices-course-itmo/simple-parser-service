package com.wine.to.up.simple.parser.service.repository;

import com.wine.to.up.simple.parser.service.domain.entity.Grapes;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**The repository that stores grape sorts info (Grapes entity).*/
@Repository
public interface GrapesRepository extends CrudRepository<Grapes, UUID> {

    /** Finding a grape sort by name
     * @param name grape name
     * @return found instance of the Grapes entity.*/
    Grapes findGrapeByGrapeName(String name);

    /** Existence check by name
     * @param name grape name*/
    Boolean existsGrapesByGrapeName(String name);
}