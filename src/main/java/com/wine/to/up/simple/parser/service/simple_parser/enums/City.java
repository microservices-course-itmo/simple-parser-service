package com.wine.to.up.simple.parser.service.simple_parser.enums;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum City {
    MOSCOW(1, "Москва"),
    SAINT_PETERSBURG(2, "Санкт-Петербург"),
    KRASNODAR(3, "Краснодар"),
    ROSTOV(4, "Ростов"),
    SOCHI(5, "Сочи"),
    NOVGOROD(6, "Новгород"),
    YEKATERINBURG(7, "Екатеринбург"),
    NOVOSIBIRSK(8, "Новосибирск"),
    CHELYABINSK(9, "Челябинск"),
    SAMARA(10, "Самара"),
    VORONEZH(11, "Воронеж"),
    UFA(12, "Уфа"),
    KAZAN(13, "Казань");

    private static final Map<Integer, City> lookup = new HashMap<>();
    private final int code;
    private final String russianName;

    static {
        for (City w : EnumSet.allOf(City.class))
            lookup.put(w.getCode(), w);
    }

    City(int code, String russianName) {
        this.code = code;
        this.russianName = russianName;
    }

    public int getCode() {
        return code;
    }

    public String getRussianName() {
        return russianName;
    }

    public static City get(int code) {
        return lookup.get(code);
    }
}
