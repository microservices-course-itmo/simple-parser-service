package com.wine.to.up.simple.parser.service.domain.entity;

import lombok.*;

import javax.persistence.*;
import java.io.File;
import java.io.FileWriter;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "wine")
@Setter
@Getter
@NoArgsConstructor
@ToString
public class Wine {
    @Id
    @Column(name = "wineid")
    private UUID wineID = UUID.randomUUID();
    @Column(name = "picture")
    private File picture;

    @OneToOne
    @JoinColumn(name = "brandid", referencedColumnName = "brandid")
    @NonNull
    private Brands brandID;

    @OneToOne
    @JoinColumn(name = "countryid", referencedColumnName = "countryid")
    @NonNull
    private Countries countryID;

    @Column(name = "volume")
    private Float volume;

    @Column(name = "abv")
    private Float abv; //alcohol by volume

    @Column(name = "colortype")
    @NonNull
    private String colorType;

    @Column(name = "sugartype")
    @NonNull
    private String sugarType;


//    @OneToMany
//    @JoinColumn(name = "winegrapesid", referencedColumnName = "winegrapesid")
//    private List<WineGrapesInfo> wineGrapesID;

    public Wine(@NonNull Brands brandID, @NonNull Countries countryID, @NonNull Float volume, @NonNull Float abv, @NonNull String colorType, @NonNull String sugarType) {
        this.brandID = brandID;
        this.countryID = countryID;
        this.volume = volume;
        this.abv = abv;
        this.colorType = colorType;
        this.sugarType = sugarType;
        //this.wineGrapesID = wineGrapesID;
    }

    @SneakyThrows
    public void writeInfoToFile(Wine someWine){
        FileWriter writer = new FileWriter("Wine.txt", false);
        writer.write(someWine.toString() + "\n");
        writer.flush();
    }

}

