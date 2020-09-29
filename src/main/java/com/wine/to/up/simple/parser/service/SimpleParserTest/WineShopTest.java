package com.wine.to.up.simple.parser.service.SimpleParserTest;

import com.wine.to.up.simple.parser.service.SimpleParser.WineShop;

import org.junit.*;

import static org.junit.Assert.*;

import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;


public class WineShopTest {

    private WineShop wineShop;

    @Before
    public void beforeTest() {
        wineShop = new WineShop(0, "name", "address", "+12345", "10:00 - 20:00");
    }

    @After
    public void afterTest() {
        wineShop = null;
    }

    @Test
    public void testToString() {
        assertEquals("WineShop{" +
                "shopID:" + wineShop.getShopID() +
                "; shopName:" + wineShop.getShopName() +
                "; shopAddress:" + wineShop.getShopAddress() +
                "; shopPhone:" + wineShop.getShopPhone() +
                "; shopWorkingHours:" + wineShop.getShopWorkingHours() +
                "}", wineShop.toString());
    }

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Test
    public void testInfoToFile() throws IOException {
        String fileName = "testFile.txt";
        File outputFile = temporaryFolder.newFile(fileName);
        wineShop.infoToFile(outputFile);
        String fileContent = new String(Files.readAllBytes(outputFile.toPath()));
        assertEquals(wineShop.toString(), fileContent);
        Files.deleteIfExists(outputFile.toPath());
    }

}
