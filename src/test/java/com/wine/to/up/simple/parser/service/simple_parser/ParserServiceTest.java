package com.wine.to.up.simple.parser.service.simple_parser;

import com.wine.to.up.commonlib.messaging.KafkaMessageSender;
import com.wine.to.up.parser.common.api.schema.ParserApi;
import com.wine.to.up.simple.parser.service.simple_parser.db_handler.WineService;
import com.wine.to.up.simple.parser.service.simple_parser.mappers.WineMapper;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.Assert.*;
import static org.modelmapper.convention.MatchingStrategies.STRICT;

import java.lang.reflect.Field;

/**
 * Class for testing {@link ParserService}
 */
@RunWith(MockitoJUnitRunner.class)
class ParserServiceTest {

    @Mock
    private KafkaMessageSender<ParserApi.WineParsedEvent> kafkaSendMessageService;
    @Mock
    private WineService wineService;
    private ParserService parserService;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
        ModelMapper modelMapper = new ModelMapper();
        modelMapper
                .getConfiguration()
                .setSkipNullEnabled(true)
                .setFieldMatchingEnabled(true)
                .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE)
                .setMatchingStrategy(STRICT)
                .setAmbiguityIgnored(true);
        WineMapper wineMapper = new WineMapper(modelMapper);
        parserService = new ParserService(kafkaSendMessageService, wineService, wineMapper);
        ReflectionTestUtils.setField(parserService, "url", "https://simplewine.ru");
        ReflectionTestUtils.setField(parserService, "wineUrl", "https://simplewine.ru/catalog/vino/page");
    }

    /**
     * Testing {@link ParserService#urlToDocument(String)} method
     * Trying to create Jsoup Document from URL
     */
    @Test
    void testURLtoDocument() {
        Document doc = ParserService.urlToDocument("https://simplewine.ru/catalog/product/lindeman_s_bin_50_shiraz_2018_075/");
        assertTrue(doc.title().contains("Вино Bin 50 Shiraz"));
    }

    @Test
    void testURLtoDocumentWrongUrl() {
        assertThrows(IllegalArgumentException.class, () -> ParserService.urlToDocument("Mem"));
    }

    @Test
    void testURLtoDocumentWrongSite() {
        Document doc = ParserService.urlToDocument("https://www.google.ru/");
        assertNotNull(doc);
    }

    @Test
    void testGetMessage() throws NoSuchFieldException, IllegalAccessException {
        Field f = ParserService.class.getDeclaredField("messageToKafka");
        f.setAccessible(true);
        assertEquals(f.get(parserService), parserService.getMessage());
    }

    @Test
    void testStartParser() {
        Assertions.assertDoesNotThrow(() -> parserService.startParser(1));
    }

    @Test
    void testStartParserNegative() {
        Assertions.assertDoesNotThrow(() -> parserService.startParser(-1));
    }

    @Test
    void testStartParserLimitExceeded() {
        Assertions.assertDoesNotThrow(() -> parserService.startParser(100500));
    }


}