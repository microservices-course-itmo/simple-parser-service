package com.wine.to.up.simple.parser.service.SimpleParser;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SimpleWine {
    private String picture;
    private String brandID;
    private String countryID;
    private Float volume;
    private Float abv; // alcohol by volume
    private String colorType;
    private String sugarType;
    private Integer year;
    private Float discount;
    private Float price;
    private String name;
    private String grapeType;
    private String region;
    private String link;
    private float rating;
    private boolean sparkling;
    private String gastronomy;
    private String taste;
}
