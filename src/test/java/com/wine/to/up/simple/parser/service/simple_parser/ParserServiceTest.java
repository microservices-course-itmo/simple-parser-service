package com.wine.to.up.simple.parser.service.simple_parser;

import com.wine.to.up.commonlib.messaging.KafkaMessageSender;
import com.wine.to.up.parser.common.api.schema.ParserApi;
import com.wine.to.up.simple.parser.service.repository.*;
import com.wine.to.up.simple.parser.service.simple_parser.mappers.WineMapper;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.Assert.*;
import static org.modelmapper.convention.MatchingStrategies.STRICT;

import java.io.IOException;
import java.lang.reflect.Field;

/**
 * Class for testing {@link ParserService}
 */
@RunWith(MockitoJUnitRunner.class)
class ParserServiceTest {
    /**
     * Testing {@link ParserService#urlToDocument(String)} method
     *
     * @throws IOException Wrong input URL string value
     */

    @InjectMocks
    private ParserService parserService;
    @Mock
    KafkaMessageSender<ParserApi.WineParsedEvent> kafkaSendMessageService;
    @Mock
    private GrapesRepository grapesRepository;
    @Mock
    private BrandsRepository brandsRepository;
    @Mock
    private CountriesRepository countriesRepository;
    @Mock
    private WineGrapesRepository wineGrapesRepository;
    @Mock
    private WineRepository wineRepository;

    private WineMapper modelMapper;

    private WineToDTO wineToDTO;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
        ReflectionTestUtils.setField(parserService, "url", "https://simplewine.ru");
        ReflectionTestUtils.setField(parserService, "wineUrl", "https://simplewine.ru/catalog/vino/page");
        ModelMapper modelMapper = new ModelMapper();
        modelMapper
                .getConfiguration()
                .setSkipNullEnabled(true)
                .setFieldMatchingEnabled(true)
                .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE)
                .setMatchingStrategy(STRICT)
                .setAmbiguityIgnored(true);
        this.modelMapper = new WineMapper(modelMapper);
        wineToDTO = new WineToDTO(this.modelMapper);
        ReflectionTestUtils.setField(parserService, "modelMapper", this.modelMapper);
        ReflectionTestUtils.setField(parserService, "wineToDTO", wineToDTO);
    }

    @Test
    void testURLtoDocument() throws IOException {
        Document doc = ParserService.urlToDocument("https://simplewine.ru/catalog/product/lindeman_s_bin_50_shiraz_2018_075/");
        assertTrue(doc.title().contains("Вино Bin 50 Shiraz"));
    }

    @Test
    void testGetMessage() throws NoSuchFieldException, IllegalAccessException {
        ParserService parserService = new ParserService();
        Field f = ParserService.class.getDeclaredField("messageToKafka");
        f.setAccessible(true);
        assertEquals(f.get(parserService), parserService.getMessage());
    }

    @Test
    void testStartParser() {
        Assertions.assertDoesNotThrow(() -> parserService.startParser(1));
    }
}