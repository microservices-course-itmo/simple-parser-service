package com.wine.to.up.simple.parser.service.SimpleParser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class WineShop {
    private int shopID;
    private String shopName;
    private String shopAddress;
    private String shopPhone;
    private String shopWorkingHours;
    private Logger logger;


    public WineShop(int shopID, String shopName, String shopAddress, String shopPhone, String shopWorkingHours) {
        this.shopID = shopID;
        this.shopName = shopName;
        this.shopAddress = shopAddress;
        this.shopPhone = shopPhone;
        this.shopWorkingHours = shopWorkingHours;
        logger = LoggerFactory.getLogger("ShopLogger");
        logger.info("The following object was created: " + this.toString());
    }

    @Override
    public String toString() {
        return "WineShop{" +
                "shopID:" + this.shopID +
                "; shopName:" + this.shopName +
                "; shopAddress:" + this.shopAddress +
                "; shopPhone:" + this.shopPhone +
                "; shopWorkingHours:" + this.shopWorkingHours +
                "}";
    }

    //log object info
    public void infoToLog() {
        logger.info(this.toString());
    }

    //write object info to file
    public void infoToFile(File file) {
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(file, true);
            fileWriter.write(this.toString());

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                assert fileWriter != null;
                fileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public int getShopID() {
        return shopID;
    }

    public void setShopID(int shopID) {
        logger.info("shopID was changed from " + this.shopID + " to " + shopID);
        this.shopID = shopID;
        logger.info("Updated object: " + this.toString());
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        logger.info("shopName was changed from " + this.shopName + " to " + shopName);
        this.shopName = shopName;
        logger.info("Updated object: " + this.toString());
    }

    public String getShopAddress() {
        return shopAddress;
    }

    public void setShopAddress(String shopAddress) {
        logger.info("shopAddress was changed from " + this.shopAddress + " to " + shopAddress);
        this.shopAddress = shopAddress;
        logger.info("Updated object: " + this.toString());
    }

    public String getShopPhone() {
        return shopPhone;
    }

    public void setShopPhone(String shopPhone) {
        logger.info("shopPhone was changed from " + this.shopPhone + " to " + shopPhone);
        this.shopPhone = shopPhone;
        logger.info("Updated object: " + this.toString());
    }

    public String getShopWorkingHours() {
        return shopWorkingHours;
    }

    public void setShopWorkingHours(String shopWorkingHours) {
        logger.info("shopWorkingHours was changed from " + this.shopWorkingHours + " to " + shopWorkingHours);
        this.shopWorkingHours = shopWorkingHours;
        logger.info("Updated object: " + this.toString());
    }
}
