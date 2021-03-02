package com.wine.to.up.simple.parser.service.simple_parser.db_handler;

import com.wine.to.up.simple.parser.service.domain.entity.Grapes;
import com.wine.to.up.simple.parser.service.repository.GrapesRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

/**
 * Class for testing {@link GrapesService}
 */
@RunWith(MockitoJUnitRunner.class)
public class GrapesServiceTest {
    private final Grapes grape = new Grapes("бонано");
    @InjectMocks
    private GrapesService grapesService;
    @Mock
    private GrapesRepository grapesRepository;

    @Test
    public void saveGrapeTest() {
        when(grapesRepository.save(grape)).thenReturn(grape);
        Grapes newGrape = grapesRepository.save(grape);
        assertEquals(grape, newGrape);
    }

    @Test
    public void findGrapeByNameTest() {
        when(grapesRepository.findGrapeByGrapeName(grape.getGrapeName())).thenReturn(grape);
        Grapes newGrape = grapesRepository.findGrapeByGrapeName("бонано");
        assertEquals(grape, newGrape);
    }

    @Test
    public void existGrapeByNameTest() {
        when(grapesRepository.existsGrapesByGrapeName(grape.getGrapeName())).thenReturn(true);
        assertEquals(true, grapesRepository.existsGrapesByGrapeName("бонано"));
    }

    @Test
    public void findAllGrapesTest() {
        ArrayList<Grapes> grapes = new ArrayList<>();
        grapes.add(grape);
        when(grapesRepository.findAll()).thenReturn(grapes);
        assertEquals(grapes, grapesRepository.findAll());
    }

    @Test
    public void testSaveGrapeService() {
        when(grapesService.saveGrape("бонано")).thenReturn(grape);
        assertEquals("бонано", grapesService.saveGrape("бонано").getGrapeName());
    }
}
