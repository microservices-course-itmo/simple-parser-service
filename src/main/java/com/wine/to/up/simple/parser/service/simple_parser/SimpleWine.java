package com.wine.to.up.simple.parser.service.simple_parser;

import com.wine.to.up.parser.common.api.schema.ParserApi;
import com.wine.to.up.simple.parser.service.domain.entity.Brands;
import com.wine.to.up.simple.parser.service.domain.entity.Countries;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Data structure for storing parsed information
 *
 * @see com.wine.to.up.simple.parser.service.domain.entity.Wine
 */
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SimpleWine {
    private String image;
    private String brand;
    private Brands brandID;
    private String country;
    private Countries countryID;
    @Builder.Default
    private Float capacity = 0.0f;
    @Builder.Default
    private Float strength = 0.0f; // alcohol by volume
    @Builder.Default
    private ParserApi.Wine.Color color = ParserApi.Wine.Color.UNDEFINED_COLOR;
    @Builder.Default
    private ParserApi.Wine.Sugar sugar = ParserApi.Wine.Sugar.UNDEFINED_SUGAR;
    private Integer year;
    private Float discount;
    private Float newPrice;
    private Float oldPrice;
    @Builder.Default
    private String name = "No info";
    private Iterable<String> grapeSort;
    @Builder.Default
    private String region = "";
    private String link;
    private float rating;
    private boolean sparkling;
    private String gastronomy;
    private String taste;
    private String city;
    @Builder.Default
    private String inStock = "Есть в наличии";
}
