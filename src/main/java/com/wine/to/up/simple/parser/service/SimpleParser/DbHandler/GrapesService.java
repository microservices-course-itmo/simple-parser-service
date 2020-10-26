package com.wine.to.up.simple.parser.service.SimpleParser.DbHandler;

import com.wine.to.up.simple.parser.service.domain.entity.Grapes;
import com.wine.to.up.simple.parser.service.repository.GrapesRepository;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class GrapesService {
    private final GrapesRepository grapesRepository;

    public GrapesService(@NonNull GrapesRepository grapesRepository) {
        this.grapesRepository = grapesRepository;
    }

    protected Grapes saveGrape(String grape) {
        if (!grapesRepository.existsGrapesByGrapeName(grape)) {
            grapesRepository.save(new Grapes(grape));
            log.trace("New Brand was added to DB: " + grape);
        }

        return grapesRepository.findGrapeByGrapeName(grape);
    }
}
