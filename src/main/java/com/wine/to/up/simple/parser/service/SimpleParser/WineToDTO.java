package com.wine.to.up.simple.parser.service.SimpleParser;

import com.wine.to.up.parser.common.api.schema.UpdateProducts;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * The class designed for wrapping wine parsed information into the common-api UpdateProducts.Product class.
 * The Proto3 protocol is used to transfer data to Kafka.
 */
@Service
@Slf4j
public class WineToDTO {
    private WineToDTO(){
        throw new IllegalStateException("Utility class");
    }
    /**
     * The function is designed to get parsed wine information which is wrapped into a common-api UpdateProducts.Product class for subsequent transfer to Kafka.
     * @param wine instance of the SimpleWine class which contains parsed wine information.
     * @return instance of UpdateProducts.Product
     **/
    public static UpdateProducts.Product getProtoWine(SimpleWine wine) {
        UpdateProducts.Product.Color color = defineColor(wine.getColorType());
        UpdateProducts.Product.Sugar sugar = defineSugar(wine.getSugarType());
        UpdateProducts.Product.Builder product = UpdateProducts.Product.newBuilder()
                .addGrapeSort(wine.getGrapeType())
                .setBrand(wine.getBrandID())
                .setCapacity(wine.getVolume())
                .setCountry(wine.getCountryID())
                .setNewPrice(wine.getPrice())
                .setYear(wine.getYear())
                .setOldPrice(100 * wine.getPrice() / (100 - wine.getDiscount()))
                .setStrength(wine.getAbv())
                .setName(wine.getName())
                .addRegion(wine.getRegion())
                .setRegion(0, wine.getRegion())
                .setLink(wine.getLink())
                .setRating(wine.getRating())
                .setSparkling(wine.isSparkling())
                .setGastronomy(wine.getGastronomy())
                .setTaste(wine.getTaste())
                .setImage(wine.getPicture());
        if (sugar != null) {
            product.setSugar(sugar);
        }
        if (color != null) {
            product.setColor(color);
        }
        if (wine.getGrapeType() != null)
            product.setGrapeSort(0, wine.getGrapeType());
        return product.build();
    }

    /**
     * The method using to define the type of wine color relative to enum common-api.
     * @param color type of color received during wine parsing.
     * @return value of UpdateProducts.Product.Color enum
     **/
    private static UpdateProducts.Product.Color defineColor(@NonNull String color) {
        UpdateProducts.Product.Color colorType;
        switch (color) {
            case "красное":
                colorType = UpdateProducts.Product.Color.RED;
                break;
            case "белое":
                colorType = UpdateProducts.Product.Color.WHITE;
                break;
            case "розовое":
                colorType = UpdateProducts.Product.Color.ROSE;
                break;
            case "оранжевое":
                colorType = UpdateProducts.Product.Color.ORANGE;
                break;
            case "":
                log.error("The wine color was not defined.");
                colorType = null;
                break;
            default:
                log.debug("Color is not from the list: {}", color);
                colorType = null;
                break;
        }
        return colorType;
    }

    /**
     * The method using to define the type of wine sugar relative to enum common-api.
     * @param sugar type of sugar received during wine parsing.
     * @return  value of UpdateProducts.Product.Sugar enum
     **/
    private static UpdateProducts.Product.Sugar defineSugar(@NonNull String sugar) {
        UpdateProducts.Product.Sugar sugarType;
        switch (sugar) {
            case "сухое":
                sugarType = UpdateProducts.Product.Sugar.DRY;
                break;
            case "полусухое":
                sugarType = UpdateProducts.Product.Sugar.MEDIUM_DRY;
                break;
            case "сладкое":
                sugarType = UpdateProducts.Product.Sugar.SWEET;
                break;
            case "полусладкое":
                sugarType = UpdateProducts.Product.Sugar.MEDIUM;
                break;
            case "":
                log.error("The wine sugar was not defined.");
                sugarType = null;
                break;
            default:
                log.debug("Sugar is not from the list: {}", sugar);
                sugarType = null;
                break;
        }
        return sugarType;
    }
}
