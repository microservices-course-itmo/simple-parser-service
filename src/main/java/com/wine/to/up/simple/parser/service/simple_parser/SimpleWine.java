package com.wine.to.up.simple.parser.service.simple_parser;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**Data structure for storing parsed information
 * @see com.wine.to.up.simple.parser.service.domain.entity.Wine*/
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SimpleWine {
    private String image;
    private String brandID;
    private String countryID;
    private Float capacity;
    private Float strength; // alcohol by volume
    private String color;
    private String sugar;
    private Integer year;
    private Float discount;
    private Float newPrice;
    private Float oldPrice;
    private String name;
    private Iterable<String> grapeSort;
    private String region;
    private String link;
    private float rating;
    private boolean sparkling;
    private String gastronomy;
    private String taste;
}
