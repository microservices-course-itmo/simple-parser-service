package com.wine.to.up.simple.parser.service.simple_parser.db_handler;

import com.wine.to.up.simple.parser.service.domain.entity.Grapes;
import com.wine.to.up.simple.parser.service.repository.GrapesRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

/**
 * The service is responsible for adding a {@link Grapes} entity to DB.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class GrapesService {
    /**
     * The repository that stores grape sorts info (name, id)
     */
    private final GrapesRepository grapesRepository;

    /**
     * Adding to DB with an existence check before
     *
     * @param grape grape sort name
     * @return instance of the {@link Grapes} entity
     * @see GrapesRepository
     */
    protected Grapes saveGrape(@NonNull String grape) {
        if (Boolean.FALSE.equals(grapesRepository.existsGrapesByGrapeName(grape))) {
            grapesRepository.save(new Grapes(grape));
            log.trace("New Brand was added to DB: {}", grape);
        }
        return grapesRepository.findGrapeByGrapeName(grape);
    }
}
