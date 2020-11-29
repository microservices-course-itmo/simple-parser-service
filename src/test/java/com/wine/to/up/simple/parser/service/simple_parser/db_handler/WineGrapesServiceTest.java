package com.wine.to.up.simple.parser.service.simple_parser.db_handler;

import com.wine.to.up.simple.parser.service.domain.entity.*;
import com.wine.to.up.simple.parser.service.repository.WineGrapesRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

/**
 * Class for testing {@link GrapesService}
 */
@RunWith(MockitoJUnitRunner.class)
public class WineGrapesServiceTest {
    private WineGrapes wineGrape;
    @InjectMocks
    private WineGrapesService wineGrapesService;
    @Mock
    private WineGrapesRepository wineGrapesRepository;

    @Before
    public void init() {
        Grapes grape = new Grapes("бонано");
        Wine wine = Wine.builder().
                name("Бин 50 Шираз").
                brandID(new Brands("Lindeman's")).
                countryID(new Countries("Австралия")).
                newPrice((float) 952.0).
                year(2018).
                capacity((float) 0.75).
                strength((float) 13.0).
                color("красное").
                grapeSort("шираз").
                sugar("полусухое").
                discount((float) 20.0).
                region("Новый Южный Уэльс").
                link("https://simplewine.ru/catalog/product/lindeman_s_bin_50_shiraz_2018_075/").
                rating((float) 4.6).
                image("https://static.simplewine.ru/upload/iblock/3ce/vino-bin-50-shiraz-lindeman-s-2018_1.png@x303").
                gastronomy("Прекрасно в сочетании с жареным ягненком, свининой с овощами и сырами средней выдержки.").
                taste("Вино блестящего фиолетово-красного цвета с яркими ароматами темных спелых ягод, ванили, лакрицы и легкими перечными нотками. " +
                        "Среднетелое, насыщенное и хорошо структурированное во вкусе, с бархатистыми танинами и оттенками черной смородины, сливы и ванили в послевкусии.").
                sparkling(false).
                build();
        wineGrape = new WineGrapes(wine, grape);
    }


    @Test
    public void saveWineGrapeTest() {
        when(wineGrapesRepository.save(wineGrape)).thenReturn(wineGrape);
        WineGrapes newWineGrape = wineGrapesRepository.save(wineGrape);
        assertEquals(wineGrape, newWineGrape);
    }

    @Test
    public void existsWineGrapesByGrapeAndAndWineTest() {
        when(wineGrapesRepository.existsWineGrapesByGrapeIdAndAndWineId(wineGrape.getGrapeId(), wineGrape.getWineId())).thenReturn(true);
        assertEquals(true, wineGrapesRepository.existsWineGrapesByGrapeIdAndAndWineId(wineGrape.getGrapeId(), wineGrape.getWineId()));
    }

    @Test
    public void findGrapeByNameTest() {
        when(wineGrapesRepository.findByGrapeIdAndAndWineId(wineGrape.getGrapeId(), wineGrape.getWineId())).thenReturn(wineGrape);
        WineGrapes newWineGrape = wineGrapesRepository.findByGrapeIdAndAndWineId(wineGrape.getGrapeId(), wineGrape.getWineId());
        assertEquals(wineGrape, newWineGrape);
    }

    @Test
    public void findAllWineGrapeTest() {
        ArrayList<WineGrapes> wineGrapes = new ArrayList<>();
        wineGrapes.add(wineGrape);
        when(wineGrapesRepository.findAll()).thenReturn(wineGrapes);
        assertEquals(wineGrapes, wineGrapesRepository.findAll());
    }

    @Test
    public void testSaveWineGrapeService() {
        when(wineGrapesService.saveWineGrapes(wineGrape.getGrapeId(), wineGrape.getWineId())).thenReturn(wineGrape);
        assertEquals(wineGrape, wineGrapesService.saveWineGrapes(wineGrape.getGrapeId(), wineGrape.getWineId()));
    }
}
