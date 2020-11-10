package com.wine.to.up.simple.parser.service.simple_parser.db_handler;

import com.wine.to.up.simple.parser.service.domain.entity.Grapes;
import com.wine.to.up.simple.parser.service.repository.GrapesRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.Assert.*;

@SpringBootTest
class GrapesServiceIT {
    @Autowired
    GrapesRepository grapesRepository;
    @Autowired
    GrapesService grapesService;

    @Test
    void testSaveGrapeNew(){
        String grapeName = "мем";
        assertEquals(grapeName, grapesService.saveGrape(grapeName).getGrapeName());
        assertEquals(grapeName, grapesRepository.findGrapeByGrapeName(grapeName).getGrapeName());
    }

    @Test
    void testSaveGrapeExisting() {
        String grapeName = "мем";
        grapesService.saveGrape(grapeName);
        grapesRepository.findGrapeByGrapeName(grapeName);
        int counter = 0;
        for (Grapes i : grapesRepository.findAll()) {
            if (i.getGrapeName().equals(grapeName)) {
                counter++;
            }
        }
        assertEquals(1, counter);
    }
}
