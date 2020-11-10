package com.wine.to.up.simple.parser.service.simple_parser;

import com.wine.to.up.parser.common.api.schema.ParserApi;
import com.wine.to.up.simple.parser.service.simple_parser.mappers.WineMapper;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;
import java.util.Map;

import static com.wine.to.up.parser.common.api.schema.ParserApi.Wine.Color.*;
import static com.wine.to.up.parser.common.api.schema.ParserApi.Wine.Color.ORANGE;
import static com.wine.to.up.parser.common.api.schema.ParserApi.Wine.Sugar.*;
import static com.wine.to.up.parser.common.api.schema.ParserApi.Wine.Sugar.SWEET;
import static org.junit.Assert.*;

@SpringBootTest
class WineToDTOTest {
    private SimpleWine wine;
    @Autowired
    private WineMapper wineMapper;

    @BeforeEach
    void initWine() {
        wine = SimpleWine.builder().
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
    }

    @ParameterizedTest
    @CsvSource({
            "красное,полусухое",
            "белое,сухое",
            "розовое,сладкое",
            "оранжевое,полусладкое",
            "фиолетовое,горькое"})
    void testGetProtoWine(String colorType, String sugarType) {
        Map<String, ParserApi.Wine.Sugar> sugarMap = Map.of( "сухое", DRY, "полусухое", MEDIUM_DRY, "полусладкое", MEDIUM, "сладкое", SWEET);
        Map<String, ParserApi.Wine.Color> colorMap = Map.of( "красное", RED, "розовое", ROSE, "белое", WHITE, "оранжевое", ORANGE);

        wine.setColor(colorMap.getOrDefault(colorType, null));
        wine.setSugar(sugarMap.getOrDefault(sugarType, null));

        ParserApi.Wine expectedProduct = wineMapper.toKafka(wine).build();
        ParserApi.Wine result = WineToDTO.getProtoWine(wine);
        assertEquals(expectedProduct.toString(), result.toString());
    }
}
