package com.wine.to.up.simple.parser.service.simple_parser.enums;

import com.wine.to.up.parser.common.api.schema.ParserApi;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

@Getter
@AllArgsConstructor
public enum Color {
    RED(ParserApi.Wine.Color.RED, "красное"),
    ROSE(ParserApi.Wine.Color.ROSE, "розовое"),
    WHITE(ParserApi.Wine.Color.WHITE, "белое"),
    UNDEFINED_COLOR(ParserApi.Wine.Color.UNDEFINED_COLOR, "нет информации");

    private final ParserApi.Wine.Color apiColor;
    private final String colorWine;

    private static final Map<String, Color> stringColorMap = new HashMap<>();
    private static final Map<ParserApi.Wine.Color, Color> apiColorHashMap = new EnumMap<>(ParserApi.Wine.Color.class);

    static {
        for (Color color : values()) {
            stringColorMap.put(color.colorWine, color);
            apiColorHashMap.put(color.apiColor, color);
        }
    }

    public static ParserApi.Wine.Color getApiColor(String color) {
        return stringColorMap.getOrDefault(color, UNDEFINED_COLOR).apiColor;
    }

    public static String getStringColor(ParserApi.Wine.Color apiColor) {
        return apiColorHashMap.getOrDefault(apiColor, UNDEFINED_COLOR).colorWine;
    }
}

