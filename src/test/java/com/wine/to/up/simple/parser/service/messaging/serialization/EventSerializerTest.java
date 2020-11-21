package com.wine.to.up.simple.parser.service.messaging.serialization;

import com.google.protobuf.InvalidProtocolBufferException;
import com.wine.to.up.parser.common.api.schema.ParserApi;
import com.wine.to.up.simple.parser.service.simple_parser.SimpleWine;
import com.wine.to.up.simple.parser.service.simple_parser.mappers.WineMapper;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.*;

import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.modelmapper.convention.MatchingStrategies.STRICT;

class EventSerializerTest {
    EventSerializer eventSerializer = new EventSerializer();

    @Test
    void testSerialize() throws InvalidProtocolBufferException {
        String testTopic = "test";
        ModelMapper modelMapper = new ModelMapper();
        modelMapper
                .getConfiguration()
                .setSkipNullEnabled(true)
                .setFieldMatchingEnabled(true)
                .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE)
                .setMatchingStrategy(STRICT)
                .setAmbiguityIgnored(true);
        WineMapper wineMapper = new WineMapper(modelMapper);
        SimpleWine testWine = SimpleWine.builder().
                name("Бин 50 Шираз").
                brand("Lindeman's").
                country("Австралия").
                newPrice((float) 952.0).
                year(2018).
                capacity((float) 0.75).
                strength((float) 13.0).
                color(ParserApi.Wine.Color.RED).
                grapeSort(Collections.singleton("шираз")).
                sugar(ParserApi.Wine.Sugar.MEDIUM_DRY).
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
        ParserApi.Wine wine = wineMapper.toKafka(testWine).build();
        List<ParserApi.Wine> testProducts = new ArrayList<>();
        testProducts.add(wine);
        ParserApi.WineParsedEvent testData = ParserApi.WineParsedEvent.newBuilder().addAllWines(testProducts).build();
        assertEquals(testData.toString(), ParserApi.WineParsedEvent.parseFrom(eventSerializer.serialize(testTopic, testData)).toString());
    }
}
