package com.wine.to.up.simple.parser.service.simple_parser.db_handler;

import com.wine.to.up.simple.parser.service.domain.entity.Wine;
import com.wine.to.up.simple.parser.service.repository.*;
import com.wine.to.up.simple.parser.service.simple_parser.SimpleWine;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;

import static org.junit.Assert.*;

/**
 * Class for testing {@link WineService}
 */
@SpringBootTest
class WineServiceTest {
    /**
     * The repository that stores all info about wine.
     */

    @Autowired
    private WineRepository wineRepository;
    @Autowired
    private WineService wineService;
    @Autowired
    private BrandsService brandsService;

    /**
     * Testing {@link WineService#saveAllWineParsedInfo(SimpleWine)} method
     */

    @Test
    void testSaveWine() {
        SimpleWine testWine = SimpleWine.builder().
                name("Бин 50 Шираз").
                brandID("Lindeman's").
                countryID("Австралия").
                newPrice((float) 952.0).
                year(2018).
                capacity((float) 0.75).
                strength((float) 13.0).
                color("красное").
                grapeSort(Collections.singleton("шираз")).
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
        wineService.saveAllWineParsedInfo(testWine);
        Assert.assertNotNull(wineRepository.findWineByLinkAndNewPrice(testWine.getLink(), testWine.getNewPrice()));
    }

    @Test
    void testDoubleSaveWine() {
        SimpleWine testWine = SimpleWine.builder().
                name("Бин 50 Шираз").
                brandID("Lindeman's").
                countryID("Австралия").
                newPrice((float) 952.0).
                year(2018).
                capacity((float) 0.75).
                strength((float) 13.0).
                color("красное").
                grapeSort(Collections.singleton("шираз")).
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
        wineService.saveAllWineParsedInfo(testWine);
        wineService.saveAllWineParsedInfo(testWine);
        int counter = 0;
        for (Wine i : wineRepository.findAll()) {
            if (testWine.getLink().equals(i.getLink())) {
                counter++;
            }
        }
        assertTrue(counter == 1);
    }

    /**
     * Testing {@link WineService#saveAllWineParsedInfo(SimpleWine)} method<br>
     * Trying to parse wine without Brand<br>
     */
    @Test
    void testParseWineWithoutBrandAndCountry()  {
        SimpleWine testWine = SimpleWine.builder().
                name("Мое любимое").
                brandID(null).
                countryID(null).
                newPrice((float) 20000.0).
                year(2018).
                capacity((float) 0.75).
                strength((float) 13.0).
                color("цветное").
                grapeSort(Collections.singleton("шираз")).
                sugar("соленое").
                discount((float) 20.0).
                region("Новый Южный").
                link("https://myfavorite").
                rating((float) 4.6).
                image("https://static.simplewine.ru/upload/iblock/3ce/vino-bin-50-shiraz-lindeman-s-2018_1.png@x303").
                gastronomy("Прекрасно в сочетании с жареным ягненком, свининой с овощами и сырами средней выдержки.").
                taste("Вино блестящего фиолетово-красного цвета с яркими ароматами темных спелых ягод, ванили, лакрицы и легкими перечными нотками. " +
                        "Среднетелое, насыщенное и хорошо структурированное во вкусе, с бархатистыми танинами и оттенками черной смородины, сливы и ванили в послевкусии.").
                sparkling(false).
                build();
        wineService.saveAllWineParsedInfo(testWine);
        assertEquals(wineRepository.existsWineByLinkAndNewPrice(testWine.getLink(), testWine.getNewPrice()), Boolean.FALSE);
    }
}


