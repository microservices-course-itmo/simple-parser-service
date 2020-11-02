package com.wine.to.up.simple.parser.service.SimpleParser.DbHandler;

import com.wine.to.up.simple.parser.service.domain.entity.Grapes;
import com.wine.to.up.simple.parser.service.repository.GrapesRepository;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/** The service is responsible for adding a grape sort to DB.*/
@Service
@Slf4j
public class GrapesService {
    /** The repository that stores grape sorts info (name, id)*/
    private final GrapesRepository grapesRepository;

    /**  The service instance creation. */
    public GrapesService(@NonNull GrapesRepository grapesRepository) {
        this.grapesRepository = grapesRepository;
    }

    /** Adding to DB with an existence check before
     * @param grape grape sort name
     * @return instance of the {@link Grapes} entity
     * @see GrapesRepository
     * */
    protected Grapes saveGrape(String grape) {
        if (Boolean.FALSE.equals(grapesRepository.existsGrapesByGrapeName(grape))) {
            grapesRepository.save(new Grapes(grape));
            log.trace("New Brand was added to DB: " + grape);
        }
        return grapesRepository.findGrapeByGrapeName(grape);
    }
}
