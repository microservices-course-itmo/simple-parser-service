package com.wine.to.up.simple.parser.service.domain.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

/**
 * The 'Brands' entity that is matched to the 'brands' DB table
 */
@Entity
@Table(name = "brands")
@Setter
@Getter
@NoArgsConstructor
@ToString
public class Brands {

    /**
     * Unique identifier
     */
    @Id
    @Column(name = "brandId")
    private UUID brandID = UUID.randomUUID();

    /**
     * Name of brand
     */
    @Column(name = "brandName")
    private String brandName;

    /**
     * The entity instance creation.
     */
    public Brands(String brandName) {
        this.brandName = brandName;
    }
}
