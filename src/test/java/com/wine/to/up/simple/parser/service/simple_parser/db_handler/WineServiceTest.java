package com.wine.to.up.simple.parser.service.simple_parser.db_handler;

import com.wine.to.up.parser.common.api.schema.ParserApi;
import com.wine.to.up.simple.parser.service.domain.entity.Brands;
import com.wine.to.up.simple.parser.service.domain.entity.Countries;
import com.wine.to.up.simple.parser.service.domain.entity.Grapes;
import com.wine.to.up.simple.parser.service.domain.entity.Wine;
import com.wine.to.up.simple.parser.service.repository.*;
import com.wine.to.up.simple.parser.service.simple_parser.SimpleWine;
import com.wine.to.up.simple.parser.service.simple_parser.mappers.WineMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;
import static org.modelmapper.convention.MatchingStrategies.STRICT;

/**
 * Testing {@link WineService#saveAllWineParsedInfo(SimpleWine)} method
 */
@RunWith(MockitoJUnitRunner.class)
public class WineServiceTest {
    /**
     * The repository that stores all info about wine.
     */
    @InjectMocks
    private WineService wineService;

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

    private Wine wine;
    private SimpleWine simpleWine;
    private Grapes grapes;
    private Brands brand;
    private Countries country;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        simpleWine = SimpleWine.builder().
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
        grapes = new Grapes("шираз");
        brand = new Brands("Lindeman's");
        country = new Countries("Австралия");
        wine = Wine.builder().
                name("Бин 50 Шираз").
                brandID(brand).
                countryID(country).
                newPrice((float) 902.0).
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
    public void saveWineTest() {
        when(wineRepository.save(wine)).thenReturn(wine);
        Wine newWine = wineRepository.save(wine);
        assertEquals(wine, newWine);
    }

    @Test
    public void existsWineByLinkAndNewPriceTest() {
        when(wineRepository.existsWineByLinkAndNewPrice(wine.getLink(), wine.getNewPrice())).thenReturn(true);
        assertEquals(true, wineRepository.existsWineByLinkAndNewPrice(wine.getLink(), wine.getNewPrice()));
    }

    @Test
    public void existsWineByManyInfoTest() {
        when(wineRepository.existsWineByNameAndNewPriceAndCapacityAndColorAndSugarAndYear(wine.getGrapeSort(), wine.getNewPrice(), wine.getCapacity(), wine.getColor(), wine.getSugar(), wine.getYear())).thenReturn(true);
        assertEquals(true, wineRepository.existsWineByNameAndNewPriceAndCapacityAndColorAndSugarAndYear(wine.getGrapeSort(), wine.getNewPrice(), wine.getCapacity(), wine.getColor(), wine.getSugar(), wine.getYear()));
    }

    @Test
    public void findWineByLinkAndNewPriceTest() {
        when(wineRepository.findWineByLinkAndNewPrice(wine.getLink(), wine.getNewPrice())).thenReturn(wine);
        Wine newWine = wineRepository.findWineByLinkAndNewPrice(wine.getLink(), wine.getNewPrice());
        assertEquals(wine, newWine);
    }

    @Test
    public void findWineByManyInfoTest() {
        when(wineRepository.findWineByNameAndNewPriceAndCapacityAndColorAndSugar(wine.getGrapeSort(), wine.getNewPrice(), wine.getCapacity(), wine.getColor(), wine.getSugar())).thenReturn(wine);
        Wine newWine = wineRepository.findWineByNameAndNewPriceAndCapacityAndColorAndSugar(wine.getGrapeSort(), wine.getNewPrice(), wine.getCapacity(), wine.getColor(), wine.getSugar());
        assertEquals(wine, newWine);
    }

    @Test
    public void findAllWineTest() {
        ArrayList<Wine> wines = new ArrayList<>();
        wines.add(wine);
        when(wineRepository.findAll()).thenReturn(wines);
        assertEquals(wines, wineRepository.findAll());
    }

    @Test
    public void testSaveAllWineInfo() {
        wineService.saveAllWineParsedInfo(simpleWine);
        assertFalse(wineRepository.existsWineByLinkAndNewPrice(wine.getLink(), wine.getNewPrice()));
    }

    @Test(expected = NullPointerException.class)
    public void testSaveWineWithoutBrandEntity() {
        wine.setBrandID(null);
        when(wineRepository.save(wine)).thenThrow(NullPointerException.class);
        wineRepository.save(wine);
    }

    @Test(expected = NullPointerException.class)
    public void testSaveWineWithoutCountryEntity() {
        wine.setCountryID(null);
        when(wineRepository.save(wine)).thenThrow(NullPointerException.class);
        wineRepository.save(wine);
    }

    @Test(expected = NullPointerException.class)
    public void testSaveWineWithoutGrapesEntity() {
        wine.setGrapeSort(null);
        when(wineRepository.save(wine)).thenThrow(NullPointerException.class);
        wineRepository.save(wine);
    }

    @Test
    public void testDoubleSaveWine() {
        AtomicInteger counter = new AtomicInteger(0);
        ArrayList<SimpleWine> list = new ArrayList<>();

        when(wineService.saveAllWineParsedInfo(simpleWine)).thenAnswer((invocation) -> {
            if (!list.contains(simpleWine)) {
                counter.incrementAndGet();
                list.add(simpleWine);
            }
            return null;
        });
        wineService.saveAllWineParsedInfo(simpleWine);
        wineService.saveAllWineParsedInfo(simpleWine);
        assertEquals(1, counter.get());
    }

    /**
     * Testing {@link WineService#saveAllWineParsedInfo(SimpleWine)} method<br>
     * Trying to parse wine without Brand and country<br>
     */
    @Test(expected = AssertionError.class)
    public void testParseWineWithoutBrandAndCountry() {
        simpleWine.setBrandID(null);
        simpleWine.setCountryID(null);
        when(wineService.saveAllWineParsedInfo(simpleWine)).thenThrow(AssertionError.class);
        wineService.saveAllWineParsedInfo(simpleWine);
    }

    /**
     * Testing {@link WineService#saveAllWineParsedInfo(SimpleWine)} method<br>
     * Trying to parse wine without Brand and country<br>
     */
    @Test
    public void testParseWineWithoutBrand() {
        simpleWine.setBrand(null);
        ArrayList<SimpleWine> list = new ArrayList<>();
        when(wineService.saveAllWineParsedInfo(simpleWine)).thenAnswer((invocation) -> {
            if (simpleWine.getBrand() != null) {
                list.add(simpleWine);
            }
            return null;
        });
        wineService.saveAllWineParsedInfo(simpleWine);
        assertNull(simpleWine.getBrand());
        assertFalse(list.contains(simpleWine));
    }

    @Test
    public void testParseWineWithoutCountry() {
        simpleWine.setCountry(null);
        ArrayList<SimpleWine> list = new ArrayList<>();
        when(wineService.saveAllWineParsedInfo(simpleWine)).thenAnswer((invocation) -> {
            if (simpleWine.getCountry() != null) {
                list.add(simpleWine);
            }
            return null;
        });
        wineService.saveAllWineParsedInfo(simpleWine);
        assertNull(simpleWine.getCountry());
        assertFalse(list.contains(simpleWine));
    }

    /**
     * Testing {@link WineService#saveAllWineParsedInfo(SimpleWine)} method<br>
     * Trying to parse wine without Grapes<br>
     */
    @Test(expected = AssertionError.class)
    public void testParseWineWithoutGrapes() {
        simpleWine.setGrapeSort(Collections.singleton(""));
        when(wineService.saveAllWineParsedInfo(simpleWine)).thenThrow(AssertionError.class);
        wineService.saveAllWineParsedInfo(simpleWine);
    }


    @Test(expected = AssertionError.class)
    public void testSaveWineWithoutLink() {
        simpleWine.setLink(null);
        when(wineService.saveAllWineParsedInfo(simpleWine)).thenThrow(AssertionError.class);
        wineService.saveAllWineParsedInfo(simpleWine);
    }

    @Test(expected = NullPointerException.class)
    public void testSaveWineWithoutPrice() {
        simpleWine.setNewPrice(null);
        when(wineService.saveAllWineParsedInfo(simpleWine)).thenThrow(NullPointerException.class);
        wineService.saveAllWineParsedInfo(simpleWine);
    }

    @Test(expected = NullPointerException.class)
    public void testSaveNull() {
        SimpleWine newWine = null;
        when(wineService.saveAllWineParsedInfo(newWine)).thenThrow(NullPointerException.class);
        wineService.saveAllWineParsedInfo(newWine);
    }

    @Test
    public void testSameGrapesSaveWine() {
        simpleWine.setGrapeSort(Arrays.asList("шираз", "шираз"));
        ArrayList<String> grapes = new ArrayList<>();
        when(wineService.saveAllWineParsedInfo(simpleWine)).thenAnswer((invocation) -> {
            for (String s : simpleWine.getGrapeSort()) {
                if (!grapes.contains(s)) {
                    grapes.add(s);
                }
            }
            return null;
        });
        wineService.saveAllWineParsedInfo(simpleWine);
        assertNotEquals(simpleWine.getGrapeSort().toString(), grapes.toString());
    }

    @Test(expected = AssertionError.class)
    public void testSaveWithoutCapacity() {
        simpleWine.setCapacity(null);
        when(wineService.saveAllWineParsedInfo(simpleWine)).thenThrow(AssertionError.class);
        wineService.saveAllWineParsedInfo(simpleWine);
    }

    @Test
    public void testSaveWineGrapes() {
        wineService.saveAllWineParsedInfo(simpleWine);
        assertNotNull(wineGrapesRepository.existsWineGrapesByGrapeIdAndAndWineId(grapesRepository.findGrapeByGrapeName("шираз"), wineRepository.findWineByLinkAndNewPrice(wine.getLink(), wine.getNewPrice())));
        assertNotNull(wineGrapesRepository.existsWineGrapesByGrapeIdAndAndWineId(grapesRepository.findGrapeByGrapeName("каберне"), wineRepository.findWineByLinkAndNewPrice(wine.getLink(), wine.getNewPrice())));
    }
}