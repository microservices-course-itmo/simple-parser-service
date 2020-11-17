package com.wine.to.up.simple.parser.service.dto;

import lombok.*;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
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
