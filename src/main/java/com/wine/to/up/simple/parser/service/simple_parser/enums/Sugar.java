package com.wine.to.up.simple.parser.service.simple_parser.enums;

import com.wine.to.up.parser.common.api.schema.ParserApi;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

@Getter
@AllArgsConstructor
public enum Sugar {
    DRY(ParserApi.Wine.Sugar.DRY, "сухое"),
    MEDIUM_DRY(ParserApi.Wine.Sugar.MEDIUM_DRY, "полусухое"),
    MEDIUM(ParserApi.Wine.Sugar.MEDIUM, "полусладкое"),
    SWEET(ParserApi.Wine.Sugar.SWEET, "сладкое");

    private final ParserApi.Wine.Sugar apiSugar;
    private final String sugarWine;

    private static final Map<String, Sugar> stringSugarMap = new HashMap<>();
    private static final Map<ParserApi.Wine.Sugar, Sugar> apiSugarMap = new EnumMap<>(ParserApi.Wine.Sugar.class);

    static {
        for (Sugar sugar : values()) {
            stringSugarMap.put(sugar.sugarWine, sugar);
            apiSugarMap.put(sugar.apiSugar, sugar);
        }
    }

    public static ParserApi.Wine.Sugar getApiSugar(String sugar) {
        return stringSugarMap.getOrDefault(sugar, DRY).apiSugar;
    }

    public static String getStringSugar(ParserApi.Wine.Sugar apiSugar) {
        return apiSugarMap.getOrDefault(apiSugar, DRY).sugarWine;
    }
}
