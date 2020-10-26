package com.wine.to.up.simple.parser.service.SimpleParser;


import com.wine.to.up.parser.common.api.schema.UpdateProducts;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class WineToDTO {

    public UpdateProducts.Product getProtoWine(SimpleWine wine) {
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

    private UpdateProducts.Product.Color defineColor(@NonNull String color) {
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
            default:
                log.error("The wine color was not defined.");
                colorType = null;
                break;
        }
        return colorType;
    }

    private UpdateProducts.Product.Sugar defineSugar(@NonNull String sugar) {
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
            default:
                log.error("The wine sugar was not defined.");
                sugarType = null;
                break;
        }
        return sugarType;
    }
}
