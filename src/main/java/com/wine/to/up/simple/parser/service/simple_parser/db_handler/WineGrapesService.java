package com.wine.to.up.simple.parser.service.simple_parser.db_handler;

import com.wine.to.up.simple.parser.service.domain.entity.Grapes;
import com.wine.to.up.simple.parser.service.domain.entity.Wine;
import com.wine.to.up.simple.parser.service.domain.entity.WineGrapes;
import com.wine.to.up.simple.parser.service.repository.WineGrapesRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * The service is responsible for adding a connection between {@link Wine} and {@link Grapes} to DB.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class WineGrapesService {
    /**
     * The repository that stores the connections between wine and its grapes.
     */
    private final WineGrapesRepository wineGrapesRepository;

    /**
     * Adding to DB with an existence check before
     *
     * @see WineGrapesRepository
     */
    protected WineGrapes saveWineGrapes(@NonNull Grapes grapeEntity, @NonNull Wine wineEntity) {
        if (Boolean.FALSE.equals(wineGrapesRepository.existsWineGrapesByGrapeIdAndAndWineId(grapeEntity, wineEntity))) {
            wineGrapesRepository.save(new WineGrapes(wineEntity, grapeEntity));
            log.trace("New Connection between Wine and Grape was added to DB");
        }
        return wineGrapesRepository.findByGrapeIdAndAndWineId(grapeEntity, wineEntity);
    }
}
