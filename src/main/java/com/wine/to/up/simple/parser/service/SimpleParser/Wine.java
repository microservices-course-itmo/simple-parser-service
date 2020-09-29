package com.wine.to.up.simple.parser.service.SimpleParser;
import lombok.*;

import java.io.File;
import java.io.FileWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.UUID;

@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Wine {
    private UUID wineID = UUID.randomUUID();
    private File picture;
    private BigDecimal brandID;
    private BigDecimal countryID;
    private Float volume;
    private Float abv; //alcohol by volume
    private String colorType;
    private String sugarType;
    private ArrayList<BigDecimal> wineGrapesID;

        public Wine(BigDecimal brandID, BigDecimal countryID, Float volume, Float abv, String colorType, String sugarType) {
        this.brandID = brandID;
        this.countryID = countryID;
        this.volume = volume;
        this.abv = abv;
        this.colorType = colorType;
        this.sugarType = sugarType;
    }

    @SneakyThrows
    public void writeInfoToFile() {
        FileWriter writer = new FileWriter("Wine.txt", false);
        writer.write(this.toString() + "\n");
        writer.flush();
    }

};
