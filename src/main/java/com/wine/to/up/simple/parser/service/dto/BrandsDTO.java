package com.wine.to.up.simple.parser.service.dto;

import lombok.*;

import java.util.UUID;

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
