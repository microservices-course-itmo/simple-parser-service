package com.wine.to.up.simple.parser.service.controller;

import com.wine.to.up.parser.common.api.schema.ParserApi;
import com.wine.to.up.simple.parser.service.domain.entity.Brands;
import com.wine.to.up.simple.parser.service.domain.entity.Countries;
import com.wine.to.up.simple.parser.service.domain.entity.Grapes;
import com.wine.to.up.simple.parser.service.domain.entity.Wine;
import com.wine.to.up.simple.parser.service.repository.BrandsRepository;
import com.wine.to.up.simple.parser.service.repository.CountriesRepository;
import com.wine.to.up.simple.parser.service.repository.GrapesRepository;
import com.wine.to.up.simple.parser.service.repository.WineRepository;
import com.wine.to.up.simple.parser.service.simple_parser.ParserService;
import com.wine.to.up.simple.parser.service.simple_parser.SimpleWine;
import com.wine.to.up.simple.parser.service.simple_parser.mappers.WineMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;

import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;
import static org.modelmapper.convention.MatchingStrategies.STRICT;

@RunWith(MockitoJUnitRunner.class)
class MainControllerTest {
    @Mock
    private GrapesRepository grapesRepository;
    @Mock
    private BrandsRepository brandsRepository;
    @Mock
    private CountriesRepository countriesRepository;
    @Mock
    private WineRepository wineRepository;
    @Mock
    private ParserService parserService;

    private MainController mainController;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
        mainController = new MainController(grapesRepository, brandsRepository, countriesRepository, wineRepository, parserService);
    }

    @Test
    void testRunParser() {
        assertEquals("Parser started by request", mainController.runParser(1));
    }

    @Test
    void testGetAllGrapes() {
        List<Grapes> grapes = new ArrayList<>();
        for (int i = 1; i < 4; i++) {
            Grapes grape = new Grapes();
            grape.setGrapeID(UUID.fromString("5211e915-c3e2-4dcb-0776-c7b900f38ab7"));
            grape.setGrapeName("Grape " + i);
            grapes.add(grape);
        }
        when(grapesRepository.findAll()).thenReturn(grapes);
        assertEquals("Grapes(grapeID=5211e915-c3e2-4dcb-0776-c7b900f38ab7, grapeName=Grape 1)<br>" +
                "Grapes(grapeID=5211e915-c3e2-4dcb-0776-c7b900f38ab7, grapeName=Grape 2)<br>" +
                "Grapes(grapeID=5211e915-c3e2-4dcb-0776-c7b900f38ab7, grapeName=Grape 3)<br>", mainController.getAllGrapes());
    }

    @Test
    void testGetAllBrands() {
        List<Brands> brands = new ArrayList<>();
        for (int i = 1; i < 4; i++) {
            Brands brand = new Brands();
            brand.setBrandID(UUID.fromString("5211e915-c3e2-4dcb-0776-c7b900f38ab7"));
            brand.setBrandName("Brand " + i);
            brands.add(brand);
        }
        when(brandsRepository.findAll()).thenReturn(brands);
        assertEquals("Brands(brandID=5211e915-c3e2-4dcb-0776-c7b900f38ab7, brandName=Brand 1)<br>" +
                "Brands(brandID=5211e915-c3e2-4dcb-0776-c7b900f38ab7, brandName=Brand 2)<br>" +
                "Brands(brandID=5211e915-c3e2-4dcb-0776-c7b900f38ab7, brandName=Brand 3)<br>", mainController.getAllBrands());
    }

    @Test
    void testGetAllCountries() {
        List<Countries> countries = new ArrayList<>();
        for (int i = 1; i < 4; i++) {
            Countries country = new Countries();
            country.setCountryID(UUID.fromString("5211e915-c3e2-4dcb-0776-c7b900f38ab7"));
            country.setCountryName("Country " + i);
            countries.add(country);
        }
        when(countriesRepository.findAll()).thenReturn(countries);
        assertEquals("Countries(countryID=5211e915-c3e2-4dcb-0776-c7b900f38ab7, countryName=Country 1)<br>" +
                "Countries(countryID=5211e915-c3e2-4dcb-0776-c7b900f38ab7, countryName=Country 2)<br>" +
                "Countries(countryID=5211e915-c3e2-4dcb-0776-c7b900f38ab7, countryName=Country 3)<br>", mainController.getAllCountries());
    }

    @Test
    void testGetAllWines() {
        List<Wine> wines = new ArrayList<>();
        for (int i = 1; i < 4; i++) {
            Wine wine = new Wine();
            wine.setWineID(UUID.fromString("5211e915-c3e2-4dcb-0776-c7b900f38ab7"));
            wine.setName("Wine " + i);
            wines.add(wine);
        }
        when(wineRepository.findAll()).thenReturn(wines);
        assertEquals("Wine(wineID=5211e915-c3e2-4dcb-0776-c7b900f38ab7, name=Wine 1," +
                        " image=null, brandID=null, countryID=null, newPrice=null, discount=null," +
                        " capacity=null, strength=null, year=0, color=null, sugar=null, grapeSort=null," +
                        " region=null, link=null, rating=null, sparkling=false, gastronomy=null, taste=null)<br>" +
                        "Wine(wineID=5211e915-c3e2-4dcb-0776-c7b900f38ab7, name=Wine 2," +
                        " image=null, brandID=null, countryID=null, newPrice=null, discount=null," +
                        " capacity=null, strength=null, year=0, color=null, sugar=null, grapeSort=null," +
                        " region=null, link=null, rating=null, sparkling=false, gastronomy=null, taste=null)<br>" +
                        "Wine(wineID=5211e915-c3e2-4dcb-0776-c7b900f38ab7, name=Wine 3," +
                        " image=null, brandID=null, countryID=null, newPrice=null, discount=null," +
                        " capacity=null, strength=null, year=0, color=null, sugar=null, grapeSort=null," +
                        " region=null, link=null, rating=null, sparkling=false, gastronomy=null, taste=null)<br>"
                , mainController.getAllWines());
    }

    @Test
    void testGetAllProducts() {
        SimpleWine simpleWine = SimpleWine.builder().
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

        List<ParserApi.Wine> products = new ArrayList<>();
        products.add(wineMapper.toKafka(simpleWine).build());
        ParserApi.WineParsedEvent message = ParserApi.WineParsedEvent.newBuilder().addAllWines(products).build();
        when(parserService.getMessage()).thenReturn(message);
        assertEquals("<a>Name: </a>Бин 60 Шираз<br><a>Link: </a>https://simplewine/catalog/product/lindeman_s_bin_50_shiraz_2018_075/<br>" +
                        "<a>Brand: </a>Lindeman's<br><a>Country: </a>Австралия<br><a>Region: </a>Новый Южный Уэльс<br>" +
                        "<a>Year: </a>2018<br><a>Grapes: </a>[шираз]<br><a>Volume: </a>0.75<br><a>ABV: </a>13.0<br><a>Sugar: </a>MEDIUM_DRY<br>" +
                        "<a>Color: </a>RED<br><a>New Price: </a>902.0<br><a>Old Price: </a>0.0<br><br>"
                , mainController.getAllProducts());
    }

    @Test
    void testHome() {
        String expectedHTML = "<ul>";
        expectedHTML += " <li><a href='/simple-parser/run-parser'>Run parser</a></li>";
        expectedHTML += " <li><a href='/simple-parser/all-wines'>Show All Wines</a></li>";
        expectedHTML += " <li><a href='/simple-parser/all-countries'>Show All Countries</a></li>";
        expectedHTML += " <li><a href='/simple-parser/all-brands'>Show All Brands</a></li>";
        expectedHTML += " <li><a href='/simple-parser/all-grapes'>Show All Grapes</a></li>";
        expectedHTML += " <li><a href='/simple-parser/all-products'>Show All Products as a Message to Kafka</a></li>";
        expectedHTML += "</ul>";
        assertEquals(expectedHTML, mainController.home());
    }
}
