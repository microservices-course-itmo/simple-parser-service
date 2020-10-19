package com.wine.to.up.simple.parser.service.domain.entity;

import lombok.*;

import javax.persistence.*;
import java.io.File;
import java.util.UUID;

@Entity
@Table(name = "wine")
@Setter
@Getter
@NoArgsConstructor
@ToString
@AllArgsConstructor
@Builder
public class Wine {
    @Id
    @Column(name = "wineId")
    private UUID wineID = UUID.randomUUID();

    @Column(name = "name")
    private String name;

    @Column(name = "picture")
    private String picture;

    @ManyToOne
    @JoinColumn(name = "brandId", referencedColumnName = "brandId")
    @NonNull
    private Brands brandID;

    @ManyToOne
    @JoinColumn(name = "countryId", referencedColumnName = "countryId")
    @NonNull
    private Countries countryID;

    @Column(name = "price")
    @NonNull
    private float price;

    @Column(name = "discount")
    private Float discount;

    @Column(name = "volume")
    @NonNull
    private Float volume;

    @Column(name = "abv")
    private Float abv; // alcohol by volume

    @Column(name = "year")
    private int year;

    @Column(name = "colorType")
    private String colorType;

    @Column(name = "sugarType")
    private String sugarType;

    @Column(name = "grapeType")
    private String grapeType;

    @Column(name = "region")
    private String region;

    @Column(name = "link")
    private String link;

    @Column(name = "rating")
    private float rating;

    @Column(name = "sparkling")
    private boolean sparkling;

    @Column(name = "gastronomy")
    private String gastronomy;

    @Column(name = "taste")
    private  String taste;
}
