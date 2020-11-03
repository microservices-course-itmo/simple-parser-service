package com.wine.to.up.simple.parser.service.simple_parser;

import com.wine.to.up.parser.common.api.schema.UpdateProducts;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.modelmapper.ModelMapper;

import java.util.Collections;

import static org.junit.Assert.*;

class WineToDTOTest {
    private SimpleWine wine;

    @BeforeEach
    void initWine() {
        wine = SimpleWine.builder().
                name("Бин 50 Шираз").
                brandID("Lindeman's").
                countryID("Австралия").
                newPrice((float) 952.0).
                year(2018).
                capacity((float) 0.75).
                strength((float) 13.0).
                grapeSort(Collections.singleton("шираз")).
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
            "красное,полусухое,RED,MEDIUM_DRY",
            "белое,сухое,WHITE,DRY",
            "розовое,сладкое,ROSE,SWEET",
            "оранжевое,полусладкое,ORANGE,MEDIUM",
            "фиолетовое,горькое, RED, DRY"})
    void testGetProtoWine(String colorType, String sugarType, UpdateProducts.Product.Color color, UpdateProducts.Product.Sugar sugar) {
        wine.setColor(colorType);
        wine.setSugar(sugarType);

        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setSkipNullEnabled(true);
        UpdateProducts.Product.Builder expectedProduct = modelMapper
                .map(wine, UpdateProducts.Product.Builder.class)
                .addAllGrapeSort(wine.getGrapeSort())
                .addRegion(wine.getRegion())
                .setColor(color)
                .setSugar(sugar);
        assertEquals(expectedProduct.toString(), WineToDTO.getProtoWine(wine).toString());
    }

    @Test
    void testGetProtoWineEmptyString() {
        wine.setColor("");
        wine.setSugar("");
        UpdateProducts.Product product = WineToDTO.getProtoWine(wine);

        assertEquals(0, product.getColor().getNumber());
        assertEquals(0, product.getSugar().getNumber());
    }
}
