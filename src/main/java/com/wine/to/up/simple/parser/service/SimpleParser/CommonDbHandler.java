package com.wine.to.up.simple.parser.service.SimpleParser;


import com.wine.to.up.parser.common.api.schema.UpdateProducts;
import lombok.NonNull;
import org.springframework.stereotype.Service;

//send what we have
@Service
public class CommonDbHandler {

    public UpdateProducts.Product putInfoToCommonDb(SimpleWine Wine) {
        UpdateProducts.Product.Color color = defineColor(Wine.getColorType());
        UpdateProducts.Product.Sugar sugar = defineSugar(Wine.getSugarType());
        UpdateProducts.Product.Builder product = UpdateProducts.Product.newBuilder()
                .setColor(color)
                .setSugar(sugar)
                .addGrapeSort(Wine.getGrapeType())
                .setGrapeSort(0, Wine.getGrapeType())
                .setBrand(Wine.getBrandID())
                .setCapacity(Wine.getVolume())
                .setCountry(Wine.getCountryID())
                .setNewPrice(Wine.getPrice())
                .setYear(Wine.getYear())
                .setOldPrice(100 * Wine.getPrice() / (100 - Wine.getDiscount()))
                .setStrength(Wine.getAbv())
                .setName(Wine.getName());

        return product.build();
    }

    private UpdateProducts.Product.Color defineColor(@NonNull String color) {
        UpdateProducts.Product.Color colorType;
        switch (color) {
            case "белое":
                colorType = UpdateProducts.Product.Color.WHITE;
                break;
            case "розовое":
                colorType = UpdateProducts.Product.Color.ROSE;
                break;
            case "оранжевое":
                colorType = UpdateProducts.Product.Color.ORANGE;
                break;
            default:
                colorType = UpdateProducts.Product.Color.RED;
                break;
        }
        return colorType;
    }

    private UpdateProducts.Product.Sugar defineSugar(@NonNull String sugar) {
        UpdateProducts.Product.Sugar sugarType;
        switch (sugar) {
            case "полусухое":
                sugarType = UpdateProducts.Product.Sugar.MEDIUM_DRY;
                break;
            case "сладкое":
                sugarType = UpdateProducts.Product.Sugar.SWEET;
                break;
            case "полусладкое":
                sugarType = UpdateProducts.Product.Sugar.MEDIUM;
                break;
            default:
                sugarType = UpdateProducts.Product.Sugar.DRY;
                break;
        }
        return sugarType;
    }
}
