package com.wine.to.up.simple.parser.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.UUID;

@EqualsAndHashCode
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GrapesDTO {
    private UUID grapeID = UUID.randomUUID();
    private String grapeName;

    public GrapesDTO(String grapeName) {
        this.grapeName = grapeName;
    }
}
