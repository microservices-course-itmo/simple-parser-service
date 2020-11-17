package com.wine.to.up.simple.parser.service.simple_parser.db_handler;

import com.wine.to.up.parser.common.api.schema.ParserApi;
import com.wine.to.up.simple.parser.service.domain.entity.Brands;
import com.wine.to.up.simple.parser.service.domain.entity.Countries;
import com.wine.to.up.simple.parser.service.domain.entity.Wine;
import com.wine.to.up.simple.parser.service.repository.*;
import com.wine.to.up.simple.parser.service.simple_parser.SimpleWine;
import com.wine.to.up.simple.parser.service.simple_parser.mappers.WineMapper;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mapstruct.BeforeMapping;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import static org.mockito.Mockito.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;
import static org.modelmapper.convention.MatchingStrategies.STRICT;

@RunWith(MockitoJUnitRunner.class)
class WineServiceTest {
    /**
     * The repository that stores all info about wine.
     */
    @Mock
    private WineRepository wineRepository;
    @Mock
    private GrapesRepository grapesRepository;
    @Mock
    private BrandsRepository brandsRepository;
    @Mock
    private CountriesRepository countriesRepository;
    @Mock
    private WineGrapesRepository wineGrapesRepository;
    @InjectMocks
    private WineService wineService;
    private SimpleWine wine;

    /**
     * Testing {@link WineService#saveAllWineParsedInfo(SimpleWine)} method
     */

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
        wineService = mock(WineService.class);
        wine = SimpleWine.builder().
                name("Бин 60 Шираз").
                brand("Lindeman's").
                country("Австралия").
                newPrice((float) 902.0).
                year(2018).
                capacity((float) 0.75).
                strength((float) 13.0).
                color(ParserApi.Wine.Color.RED).
                grapeSort(Collections.singleton("шираз")).
                sugar(ParserApi.Wine.Sugar.MEDIUM_DRY).
                discount((float) 20.0).
                region("Новый Южный Уэльс").
                link("https://simplewine/catalog/product/lindeman_s_bin_50_shiraz_2018_075/").
                rating((float) 4.6).
                image("https://static.simplewine.ru/upload/iblock/3ce/vino-bin-50-shiraz-lindeman-s-2018_1.png@x303").
                gastronomy("Прекрасно в сочетании с жареным ягненком, свининой с овощами и сырами средней выдержки.").
                taste("Вино блестящего фиолетово-красного цвета с яркими ароматами темных спелых ягод, ванили, лакрицы и легкими перечными нотками. " +
                        "Среднетелое, насыщенное и хорошо структурированное во вкусе, с бархатистыми танинами и оттенками черной смородины, сливы и ванили в послевкусии.").
                sparkling(false).
                build();
        ModelMapper modelMapper = new ModelMapper();
        modelMapper
                .getConfiguration()
                .setSkipNullEnabled(true)
                .setFieldMatchingEnabled(true)
                .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE)
                .setMatchingStrategy(STRICT)
                .setAmbiguityIgnored(true);
        WineMapper wineMapper = new WineMapper(modelMapper);
        ReflectionTestUtils.setField(wineService, "wineMapper", wineMapper);
    }

    @Test
    void testSaveAllWineInfo() {
        wineService.saveAllWineParsedInfo(wine);
        assertFalse(wineRepository.existsWineByLinkAndNewPrice(wine.getLink(), wine.getNewPrice()));
    }

    @Test
    void testDoubleSaveWine() {
        AtomicInteger counter = new AtomicInteger(0);
        ArrayList<SimpleWine> list = new ArrayList();

        Mockito.doAnswer((invocation) -> {
            if (!list.contains(wine)) {
                counter.incrementAndGet();
                list.add(wine);
            }
            return null;
        }).when(wineService).saveAllWineParsedInfo(wine);
        wineService.saveAllWineParsedInfo(wine);
        wineService.saveAllWineParsedInfo(wine);
        assertEquals(1, counter.get());
    }

    /**
     * Testing {@link WineService#saveAllWineParsedInfo(SimpleWine)} method<br>
     * Trying to parse wine without Brand and country<br>
     */
    @Test
    void testParseWineWithoutBrandAndCountry() {
        wineService.saveAllWineParsedInfo(wine);
        assertFalse(wineRepository.existsWineByLinkAndNewPrice(wine.getLink(), wine.getNewPrice()));
    }

    /**
     * Testing {@link WineService#saveAllWineParsedInfo(SimpleWine)} method<br>
     * Trying to parse wine without Brand and country<br>
     */
    @Test
    void testParseWineWithoutBrand() {
        wine.setBrand(null);
        ArrayList<SimpleWine> list = new ArrayList();
        Mockito.doAnswer((invocation) -> {
            if (wine.getBrand() != null) {
                list.add(wine);
            }
            return null;
        }).when(wineService).saveAllWineParsedInfo(wine);
        wineService.saveAllWineParsedInfo(wine);
        assertNull(wine.getBrand());
        assertFalse(list.contains(wine));
    }

    /**
     * Testing {@link WineService#saveAllWineParsedInfo(SimpleWine)} method<br>
     * Trying to parse wine without Grapes<br>
     */
    @Test
    void testParseWineWithoutGrapes() throws NullPointerException {
        wine.setGrapeSort(null);
        Mockito.doThrow(new NullPointerException()).when(wineService).saveAllWineParsedInfo(wine);
        assertNull(wine.getGrapeSort());
        assertThrows(NullPointerException.class, () -> wineService.saveAllWineParsedInfo(wine));
    }

    @Test
    void testCreateConstructor() {
        //Todo:
        //whenNew(Bicycle.class).withArguments(tested).thenThrow(new RemoteException());
        //Mockito.doThrow(new NullPointerException()).when(wineService).saveAllWineParsedInfo(wine);
        //PowerMockito.whenNew(Second.class).withNoArguments().thenReturn(second);
        //assertThrows(NullPointerException.class, () -> new WineService(null, null, null, null, null, null));
    }

    @Test
    void testSaveWineWithoutLink() {
        wine.setLink(null);
        Mockito.doThrow(new NullPointerException()).when(wineService).saveAllWineParsedInfo(wine);
        assertNull(wine.getLink());
        assertThrows(NullPointerException.class, () -> wineService.saveAllWineParsedInfo(wine));
    }

    @Test
    void testSaveWineWithoutPrice() throws NullPointerException {
        wine.setNewPrice(null);
        Mockito.doThrow(new NullPointerException()).when(wineService).saveAllWineParsedInfo(wine);
        assertNull(wine.getNewPrice());
        assertThrows(NullPointerException.class, () -> wineService.saveAllWineParsedInfo(wine));
    }

    @Test
    void testSaveNull() throws NullPointerException {
        Mockito.doThrow(new NullPointerException()).when(wineService).saveAllWineParsedInfo(null);
        assertThrows(NullPointerException.class, () -> wineService.saveAllWineParsedInfo(null));
    }

    @Test
    void testSameGrapesSaveWine() {
        wine.setGrapeSort(Arrays.asList("шираз", "шираз"));
        ArrayList<String> grapes = new ArrayList();
        Mockito.doAnswer((invocation) -> {
            for(String s: wine.getGrapeSort()){
                if(!grapes.contains(s)){
                    grapes.add(s);
                }
            }
            return null;
        }).when(wineService).saveAllWineParsedInfo(wine);
        wineService.saveAllWineParsedInfo(wine);
        assertNotEquals(wine.getGrapeSort().toString(), grapes.toString());
    }


    @Test
    void testSaveWithoutCapacity() {
        wine.setCapacity(null);
        Mockito.doThrow(new DataIntegrityViolationException("message")).when(wineService).saveAllWineParsedInfo(wine);
        assertNull(wine.getCapacity());
        assertThrows(DataIntegrityViolationException.class, () -> wineService.saveAllWineParsedInfo(wine));
    }


    @Test
    void testSaveWineGrapes() {
        wineService.saveAllWineParsedInfo(wine);
        assertNotNull(wineGrapesRepository.existsWineGrapesByGrapeIdAndAndWineId(grapesRepository.findGrapeByGrapeName("шираз"), wineRepository.findWineByLinkAndNewPrice(wine.getLink(), wine.getNewPrice())));
        assertNotNull(wineGrapesRepository.existsWineGrapesByGrapeIdAndAndWineId(grapesRepository.findGrapeByGrapeName("каберне"), wineRepository.findWineByLinkAndNewPrice(wine.getLink(), wine.getNewPrice())));
    }
}