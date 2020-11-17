package com.wine.to.up.simple.parser.service.simple_parser.db_handler;

import com.wine.to.up.parser.common.api.schema.ParserApi;
import com.wine.to.up.simple.parser.service.domain.entity.Wine;
import com.wine.to.up.simple.parser.service.repository.*;
import com.wine.to.up.simple.parser.service.simple_parser.SimpleWine;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.*;

/**
 * Class for testing {@link WineService}
 */
@SpringBootTest
class WineServiceIT {
    /**
     * The repository that stores all info about wine.
     */
    @Autowired
    private WineRepository wineRepository;
    @Autowired
    private WineGrapesRepository wineGrapesRepository;
    @Autowired
    private GrapesRepository grapesRepository;
    @Autowired
    private WineService wineService;

    /**
     * Testing {@link WineService#saveAllWineParsedInfo(SimpleWine)} method
     */

    @Test
    void testSaveAllWineInfo() {
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
        wineService.saveAllWineParsedInfo(testWine);
        assertNotNull(wineRepository.findWineByLinkAndNewPrice(testWine.getLink(), testWine.getNewPrice()));
    }

    @Test
    void testDoubleSaveWine() {
        SimpleWine testWine = SimpleWine.builder().
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
        wineService.saveAllWineParsedInfo(testWine);
        wineService.saveAllWineParsedInfo(testWine);
        int counter = 0;
        for (Wine i : wineRepository.findAll()) {
            if (testWine.getLink().equals(i.getLink())) {
                counter++;
            }
        }
        assertEquals(1, counter);
    }

    /**
     * Testing {@link WineService#saveAllWineParsedInfo(SimpleWine)} method<br>
     * Trying to parse wine without Brand and country<br>
     */
    @Test
    void testParseWineWithoutBrandAndCountry() {
        SimpleWine testWine = SimpleWine.builder().
                name("Мое любимое").
                brand(null).
                country(null).
                newPrice((float) 20000.0).
                year(2018).
                capacity((float) 0.75).
                strength((float) 13.0).
                color(ParserApi.Wine.Color.UNRECOGNIZED).
                grapeSort(Collections.singleton("шираз")).
                sugar(ParserApi.Wine.Sugar.UNRECOGNIZED).
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
        assertFalse(wineRepository.existsWineByLinkAndNewPrice(testWine.getLink(), testWine.getNewPrice()));
    }

    /**
     * Testing {@link WineService#saveAllWineParsedInfo(SimpleWine)} method<br>
     * Trying to parse wine without Brand and country<br>
     */
    @Test
    void testParseWineWithoutBrand() {
        SimpleWine testWine = SimpleWine.builder().
                name("Мое любимое").
                brand(null).
                country("Раися").
                newPrice((float) 20000.0).
                year(2018).
                capacity((float) 0.75).
                strength((float) 13.0).
                color(ParserApi.Wine.Color.UNRECOGNIZED).
                grapeSort(Collections.singleton("шираз")).
                sugar(ParserApi.Wine.Sugar.UNRECOGNIZED).
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
        assertFalse(wineRepository.existsWineByLinkAndNewPrice(testWine.getLink(), testWine.getNewPrice()));
    }

    /**
     * Testing {@link WineService#saveAllWineParsedInfo(SimpleWine)} method<br>
     * Trying to parse wine without Grapes<br>
     */
    @Test
    void testParseWineWithoutGrapes() throws NullPointerException {
        SimpleWine testWine = SimpleWine.builder().
                name("Мое любимое").
                brand("Чайка").
                country("Раися").
                newPrice((float) 20000.0).
                year(2018).
                capacity((float) 0.75).
                strength((float) 13.0).
                color(ParserApi.Wine.Color.UNRECOGNIZED).
                grapeSort(null).
                sugar(ParserApi.Wine.Sugar.UNRECOGNIZED).
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
        assertThrows(NullPointerException.class, () -> wineService.saveAllWineParsedInfo(testWine));
    }

    @Test
    void testCreateConstructor() {
        assertThrows(NullPointerException.class, () -> new WineService(null, null, null, null, null, null));
    }

    @Test
    void testSaveWineWithoutLink() {
        SimpleWine testWine = SimpleWine.builder().
                name("Мое любимое").
                brand("Чайка").
                country("Раися").
                newPrice((float) 20000.0).
                year(2018).
                capacity((float) 0.75).
                strength((float) 13.0).
                color(ParserApi.Wine.Color.UNRECOGNIZED).
                grapeSort(null).
                sugar(ParserApi.Wine.Sugar.UNRECOGNIZED).
                discount((float) 20.0).
                region("Новый Южный").
                link(null).
                rating((float) 4.6).
                image("https://static.simplewine.ru/upload/iblock/3ce/vino-bin-50-shiraz-lindeman-s-2018_1.png@x303").
                gastronomy("Прекрасно в сочетании с жареным ягненком, свининой с овощами и сырами средней выдержки.").
                taste("Вино блестящего фиолетово-красного цвета с яркими ароматами темных спелых ягод, ванили, лакрицы и легкими перечными нотками. " +
                        "Среднетелое, насыщенное и хорошо структурированное во вкусе, с бархатистыми танинами и оттенками черной смородины, сливы и ванили в послевкусии.").
                sparkling(false).
                build();
        assertThrows(NullPointerException.class, () -> wineService.saveAllWineParsedInfo(testWine));
    }

    @Test
    void testSaveWineWithoutPrice() throws NullPointerException {
        SimpleWine testWine = SimpleWine.builder().
                name("Мое любимое").
                brand("Чайка").
                country("Раися").
                newPrice(null).
                year(2018).
                capacity((float) 0.75).
                strength((float) 13.0).
                color(ParserApi.Wine.Color.UNRECOGNIZED).
                grapeSort(null).
                sugar(ParserApi.Wine.Sugar.UNRECOGNIZED).
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
        assertThrows(NullPointerException.class, () -> wineService.saveAllWineParsedInfo(testWine));
    }

    @Test
    void testSaveNull() throws NullPointerException {
        assertThrows(NullPointerException.class, () -> wineService.saveAllWineParsedInfo(null));
    }

    @Test
    void testSameGrapesSaveWine() {
        SimpleWine testWine = SimpleWine.builder().
                name("Бин 50 Шираз").
                brand("Lindeman's").
                country("Австралия").
                newPrice((float) 952.0).
                year(2018).
                capacity((float) 0.75).
                strength((float) 13.0).
                color(ParserApi.Wine.Color.RED).
                grapeSort(Arrays.asList("шираз", "шираз")).
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
        wineService.saveAllWineParsedInfo(testWine);
        assertNotEquals(testWine.getGrapeSort().toString(), wineRepository.findWineByLinkAndNewPrice(testWine.getLink(), testWine.getNewPrice()).getGrapeSort());
    }


    @Test
    void testSaveWithoutCapacity() {
        SimpleWine testWine = SimpleWine.builder().
                name("Мое любимое2").
                brand("Чайка").
                country("Раися").
                newPrice((float) 3000.0).
                year(2018).
                strength((float) 13.0).
                color(ParserApi.Wine.Color.UNRECOGNIZED).
                grapeSort(Arrays.asList("шираз", "каберне")).
                sugar(ParserApi.Wine.Sugar.UNRECOGNIZED).
                discount((float) 20.0).
                region("Новый Южный").
                link("https://myfavorite2").
                rating((float) 4.6).
                image("https://static.simplewine.ru/upload/iblock/3ce/vino-bin-50-shiraz-lindeman-s-2018_1.png@x303").
                gastronomy("Прекрасно в сочетании с жареным ягненком, свининой с овощами и сырами средней выдержки.").
                taste("Вино блестящего фиолетово-красного цвета с яркими ароматами темных спелых ягод, ванили, лакрицы и легкими перечными нотками. " +
                        "Среднетелое, насыщенное и хорошо структурированное во вкусе, с бархатистыми танинами и оттенками черной смородины, сливы и ванили в послевкусии.").
                sparkling(false).
                build();
        assertThrows(DataIntegrityViolationException.class, () -> wineService.saveAllWineParsedInfo(testWine));
    }


    @Test
    void testSaveWineGrapes() {
        SimpleWine testWine = SimpleWine.builder().
                name("Мое любимое").
                brand("Чайка").
                country("Раися").
                newPrice((float) 30000.0).
                year(2018).
                capacity((float) 0.75).
                strength((float) 13.0).
                color(ParserApi.Wine.Color.UNRECOGNIZED).
                grapeSort(Arrays.asList("шираз", "каберне")).
                sugar(ParserApi.Wine.Sugar.UNRECOGNIZED).
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

        assertNotNull(wineGrapesRepository.existsWineGrapesByGrapeIdAndAndWineId(grapesRepository.findGrapeByGrapeName("шираз"), wineRepository.findWineByLinkAndNewPrice(testWine.getLink(), testWine.getNewPrice())));
        assertNotNull(wineGrapesRepository.existsWineGrapesByGrapeIdAndAndWineId(grapesRepository.findGrapeByGrapeName("каберне"), wineRepository.findWineByLinkAndNewPrice(testWine.getLink(), testWine.getNewPrice())));
    }
}



