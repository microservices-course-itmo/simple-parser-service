package com.wine.to.up.simple.parser.service.dto;

import com.wine.to.up.simple.parser.service.domain.entity.Brands;
import com.wine.to.up.simple.parser.service.domain.entity.Countries;
import lombok.*;

import java.util.UUID;

@EqualsAndHashCode
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WineDTO {
    private UUID wineID = UUID.randomUUID();
    private String name;
    private String image;
    private Brands brandID;
    private Countries countryID;
    private Float newPrice;
    private Float discount;
    private Float capacity;
    private Float strength;
    private int year;
    private String color;
    private String sugar;
    private String grapeSort;
    private String region;
    private String link;
    private Float rating;
    private boolean sparkling;
    private String gastronomy;
    private String taste;
    private String city;
    private Integer inStock;
}
