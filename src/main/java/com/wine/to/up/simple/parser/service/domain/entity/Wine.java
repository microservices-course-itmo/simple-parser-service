package com.wine.to.up.simple.parser.service.domain.entity;

import lombok.*;

import javax.persistence.*;
import java.util.UUID;

/**
 * The 'Wine' entity that is matched to the 'wine' DB table
 */
@Entity
@Table(name = "wine")
@Setter
@Getter
@NoArgsConstructor
@ToString
@AllArgsConstructor
@Builder
public class Wine {

    /**
     * Unique identifier
     */
    @Id
    @Column(name = "wineId")
    private UUID wineID = UUID.randomUUID();

    /**
     * Name of wine
     */
    @Column(name = "name")
    private String name;

    /**
     * Link to a wine picture
     */
    @Column(name = "picture")
    private String image;

    /**
     * Foreign key that refers to the {@link Brands} entity
     */
    @ManyToOne
    @JoinColumn(name = "brandId", referencedColumnName = "brandId")
    @NonNull
    private Brands brandID;

    /**
     * Foreign key that refers to the {@link Countries} entity
     */
    @ManyToOne
    @JoinColumn(name = "countryId", referencedColumnName = "countryId")
    @NonNull
    private Countries countryID;

    /**
     * Wine price
     */
    @Column(name = "price")
    @NonNull
    private Float newPrice;

    /**
     * Wine discount. By default 0.
     */
    @Column(name = "discount")
    private Float discount;

    /**
     * Wine bottle volume
     */
    @Column(name = "volume")
    @NonNull
    private Float capacity;

    /**
     * Alcohol by volume
     */
    @Column(name = "abv")
    private Float strength;

    /**
     * A wine vintage is the year in which the grapes were harvested.
     */
    @Column(name = "year")
    private int year;

    /**
     * Color of wine. Characteristics of wine.
     */
    @Column(name = "colorType")
    private String color;

    /**
     * Sugar of wine. Characteristics of wine.
     */
    @Column(name = "sugarType")
    private String sugar;

    /**
     * Grape types used to make wine
     */
    @Column(name = "grapeType")
    private String grapeSort;

    /**
     * Grape-growing and wine manufacturing region
     */
    @Column(name = "region")
    private String region;

    /**
     * Link to the wine page
     */
    @Column(name = "link")
    private String link;

    /**
     * Wine rating on Simplewine website
     */
    @Column(name = "rating")
    private Float rating;

    /**
     * Sparkling flag. 0 - simple wine, 1 - sparkling wine
     */
    @Column(name = "sparkling")
    private boolean sparkling;

    /**
     * What will accompany this wine well.
     */
    @Column(name = "gastronomy")
    private String gastronomy;

    /**
     * Tasting characteristics.
     */
    @Column(name = "taste")
    private String taste;

    /**
     * City from which the wine was parsed
     */
    @Column(name = "city")
    private String city;

    /**
     * Amount of wine in stock
     */
    @Column(name = "inStock")
    private Integer inStock;
}
