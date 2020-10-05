package com.wine.to.up.simple.parser.service.SimpleParser;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.UUID;

public class Wine {
    private UUID wineID = UUID.randomUUID();
    private File picture;
    private String brandID;
    private String countryID;
    private Float volume;
    private Float abv; // alcohol by volume
    private String colorType;
    private String sugarType;
    private int year;
    private String price;
    private String name;
    private ArrayList<BigDecimal> wineGrapesID;

    public Wine(String name, String brandID, String countryID, String price, Float volume, Float abv, String colorType,
            String sugarType) {
        this.name = name;
        this.brandID = brandID;
        this.price = price;
        this.countryID = countryID;
        this.volume = volume;
        this.abv = abv;
        this.colorType = colorType;
        this.sugarType = sugarType;
    }

    public void writeInfoToFile() {
        FileWriter writer;
        try {
            writer = new FileWriter("Wine.txt", true);
            writer.write(this.toString() + "\n");
            writer.flush();
            writer.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    @Override
    public String toString() {
        return "{name=" + this.name + ", brandID=" + this.brandID + ", countryID=" + this.countryID + ", price="
                + this.price + ", volume=" + this.volume + ", abv=" + this.abv + ", colorType=" + this.colorType
                + ", sugarType=" + this.sugarType + '}';
    }
}
