package com.wine.to.up.simple.parser.service.dto;
import lombok.*;
import org.springframework.stereotype.Component;
import java.util.UUID;


@Component
@EqualsAndHashCode
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BrandsDTO {

    private UUID brandID = UUID.randomUUID();
    private String brandName;

    public BrandsDTO(String brandName) {
        this.brandName = brandName;
    }

}
