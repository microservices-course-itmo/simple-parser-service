package com.wine.to.up.simple.parser.service.dto;

import lombok.*;

import java.util.UUID;

@EqualsAndHashCode
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CountriesDTO {
    private UUID countryID = UUID.randomUUID();
    private String countryName;

    public CountriesDTO(String countryName) {
        this.countryName = countryName;
    }
}
