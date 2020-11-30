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
 * The 'Countries' entity that is matched to the 'countries' DB table
 */
@Entity
@Table(name = "countries")
@Setter
@Getter
@NoArgsConstructor
@ToString
public class Countries {
    /**
     * Unique identifier
     */
    @Id
    @Column(name = "countryId")
    private UUID countryID = UUID.randomUUID();

    /**
     * Name of country
     */
    @Column(name = "countryName")
    private String countryName;

    /**
     * The entity instance creation.
     */
    public Countries(String countryName) {
        this.countryName = countryName;
    }
}

