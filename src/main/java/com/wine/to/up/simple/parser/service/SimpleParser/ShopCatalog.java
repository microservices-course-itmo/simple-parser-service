package com.wine.to.up.simple.parser.service.SimpleParser;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class ShopCatalog {

    private int wineID;
    private int wineShopID;
    private int shopID;
    private int price;

    //ArrayList<Shops> shops = new ArrayList<Shops>(); - возможно нужно будет добавить сюда лист магазинов

    public int getWineID() {
        return wineID;
    }

    public void setWineID(int wineID) {
        this.wineID = wineID;
    }

    public int getWineShopID() {
        return wineShopID;
    }

    public void setWineShopID(int wineShopID) {
        this.wineShopID = wineShopID;
    }

    public int getShopID() {
        return shopID;
    }

    public void setShopID(int shopID) {
        this.shopID = shopID;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }


    public ShopCatalog(int wineID, int wineShopID, int shopID, int price){
        this.wineID = wineID;
        this.wineShopID = wineShopID;
        this.shopID = shopID;
        this.price = price;
    }

    //первый аргумент - путь к файлу, второй - каталог
    public void writeToFile(String filePath, ShopCatalog shopCatalog) {
        File file = new File(filePath);
        FileWriter fr = null;
        try {
            fr = new FileWriter(file,true);
            String shopInfo = "wine ID: " + getWineID() + " wineShop ID: " + getWineShopID() + " shop ID: " + getShopID() + " price: " + getPrice() + System.getProperty("line.separator");
            fr.write(shopInfo);

        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            try {
                assert fr != null;
                fr.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public String toString() {
        return "ShopCatalog{" +
                "wineID=" + wineID +
                ", wineShopID=" + wineShopID +
                ", shopID=" + shopID +
                ", price=" + price +
                '}';
    }
}

