package com.wine.to.up.simple.parser.service.SimpleParser;

import org.junit.*;

import static org.junit.Assert.*;

import org.junit.rules.TemporaryFolder;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;


public class ShopCatalogTest {

    private ShopCatalog shopCatalog;

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Before
    public void beforeTest() {
        WineShop wineShop = new WineShop(0, "name", "address", "+12345", "10:00 - 20:00");
        shopCatalog = new ShopCatalog(0, 1, 2, 100, wineShop);
    }


    @Test
    public void testToString() {
        assertEquals("ShopCatalog(" +
                "wineID=" + shopCatalog.getWineID() +
                ", wineShopID=" + shopCatalog.getWineShopID() +
                ", shopID=" + shopCatalog.getShopID() +
                ", price=" + shopCatalog.getPrice() +
                ", newShop=" + shopCatalog.getNewShop().toString() +
                ")", shopCatalog.toString());
    }


    @Test
    public void testWriteToFile() throws IOException {
        String fileName = "testFile.txt";
        File outputFile = temporaryFolder.newFile(fileName);
        shopCatalog.writeToFile(outputFile.toPath().toString());
        String fileContent = new String(Files.readAllBytes(outputFile.toPath()));
        assertEquals(shopCatalog.toString(), fileContent);
        Files.deleteIfExists(outputFile.toPath());
    }
}

