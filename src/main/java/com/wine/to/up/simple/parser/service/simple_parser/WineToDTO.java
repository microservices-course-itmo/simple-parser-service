package com.wine.to.up.simple.parser.service.simple_parser;

import com.wine.to.up.parser.common.api.schema.ParserApi;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

/**
 * The class designed for wrapping wine parsed information into the common-api ParserApi.WineParsedEvent class.
 * The Proto3 protocol is used to transfer data to Kafka.
 */
@Service
@Slf4j
public class WineToDTO {
    private WineToDTO() {
    }

    /**
     * The function is designed to get parsed wine information which is wrapped into a common-api ParserApi.WineParsedEvent class for subsequent transfer to Kafka.
     *
     * @param wine instance of the SimpleWine class which contains parsed wine information.
     * @return instance of ParserApi.WineParsedEvent
     **/
    public static ParserApi.Wine getProtoWine(SimpleWine wine) {
        ParserApi.Wine.Color color = defineColor(wine.getColor());
        ParserApi.Wine.Sugar sugar = defineSugar(wine.getSugar());
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setSkipNullEnabled(true);
        ParserApi.Wine.Builder product = modelMapper
                .map(wine, ParserApi.Wine.Builder.class)
                .addAllGrapeSort(wine.getGrapeSort())
                .addRegion(wine.getRegion());

        if (color != null) {
            try {
                product.setColor(color);
            }
            catch (IllegalArgumentException e){
                log.error("Can`t set color - UNRECOGNIZED");
            }

        }
        if (sugar != null) {
            try {
                product.setSugar(sugar);
            }
            catch (IllegalArgumentException e){
                log.error("Can`t set sugar - UNRECOGNIZED");
            }
        }
        return product.build();
    }

    /**
     * The method using to define the type of wine color relative to enum common-api.
     *
     * @param color type of color received during wine parsing.
     * @return value of ParserApi.WineParsedEvent.Color enum
     **/
    private static ParserApi.Wine.Color defineColor(String color) {
        ParserApi.Wine.Color colorType;
        switch (color) {
            case "красное":
                colorType = ParserApi.Wine.Color.RED;
                break;
            case "белое":
                colorType = ParserApi.Wine.Color.WHITE;
                break;
            case "розовое":
                colorType = ParserApi.Wine.Color.ROSE;
                break;
            case "оранжевое":
                colorType = ParserApi.Wine.Color.ORANGE;
                break;
            case "":
                log.error("The wine color was not defined.");
                colorType = ParserApi.Wine.Color.UNRECOGNIZED;
                break;
            default:
                log.debug("Color is not from the list: {}", color);
                colorType = ParserApi.Wine.Color.UNRECOGNIZED;
                break;
        }
        return colorType;
    }

    /**
     * The method using to define the type of wine sugar relative to enum common-api.
     *
     * @param sugar type of sugar received during wine parsing.
     * @return value of ParserApi.WineParsedEvent.Sugar enum
     **/
    private static ParserApi.Wine.Sugar defineSugar(String sugar) {
        ParserApi.Wine.Sugar sugarType;
        switch (sugar) {
            case "сухое":
                sugarType = ParserApi.Wine.Sugar.DRY;
                break;
            case "полусухое":
                sugarType = ParserApi.Wine.Sugar.MEDIUM_DRY;
                break;
            case "сладкое":
                sugarType = ParserApi.Wine.Sugar.SWEET;
                break;
            case "полусладкое":
                sugarType = ParserApi.Wine.Sugar.MEDIUM;
                break;
            case "":
                log.error("The wine sugar was not defined.");
                sugarType = ParserApi.Wine.Sugar.UNRECOGNIZED;
                break;
            default:
                log.debug("Sugar is not from the list: {}", sugar);
                sugarType = ParserApi.Wine.Sugar.UNRECOGNIZED;
                break;
        }
        return sugarType;
    }
}
