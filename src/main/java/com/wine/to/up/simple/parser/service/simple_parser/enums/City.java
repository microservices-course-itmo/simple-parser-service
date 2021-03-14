package com.wine.to.up.simple.parser.service.simple_parser.enums;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum City {
    MOSCOW(1),
    SAINT_PETERSBURG(2),
    KRASNODAR(3),
    ROSTOV(4),
    SOCHI(5),
    NOVGOROD(6),
    YEKATERINBURG(7),
    NOVOSIBIRSK(8),
    CHELYABINSK(9),
    SAMARA(10),
    VORONEZH(11),
    UFA(12),
    KAZAN(13);

    private static final Map<Integer, City> lookup = new HashMap<>();
    private int code;

    static {
        for (City w : EnumSet.allOf(City.class))
            lookup.put(w.getCode(), w);
    }

    City(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static City get(int code) {
        return lookup.get(code);
    }
}
