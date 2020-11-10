package com.wine.to.up.simple.parser.service.domain.entity;

import lombok.*;

import javax.persistence.*;
import java.util.UUID;

/** The 'WineGrapes' entity that is matched to the 'wine_grapes' DB table  */
@Entity
@Table(name = "wineGrapes")
@Setter
@Getter
@ToString
@NoArgsConstructor
public class WineGrapes {

    /** Unique identifier */
    @Id
    @Column(name = "id")
    private UUID id = UUID.randomUUID();

    /** Foreign key that refers to the {@link Wine} entity */
    @ManyToOne
    @JoinColumn(name = "wineId", referencedColumnName = "wineId")
    @NonNull
    private Wine wineId;

    /** Foreign key that refers to the {@link Grapes} entity */
    @ManyToOne
    @NonNull
    @JoinColumn(name = "grapeId", referencedColumnName = "grapeId")
    private Grapes grapeId;

    /**  The entity instance creation. */
    public WineGrapes(Wine wineID, Grapes grapeID) {
        this.grapeId = grapeID;
        this.wineId = wineID;
    }
}
