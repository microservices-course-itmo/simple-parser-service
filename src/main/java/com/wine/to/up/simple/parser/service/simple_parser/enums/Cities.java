package com.wine.to.up.simple.parser.service.simple_parser.enums;

public enum Cities {
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

    private final int number;

    Cities(int number) {
        this.number = number;
    }

    public int getNumber() {
        return number;
    }
}
