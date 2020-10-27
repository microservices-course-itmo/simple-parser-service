package com.wine.to.up.simple.parser.service.domain.entity;

import lombok.*;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

/** The 'Grapes' entity that is matched to the 'grapes' DB table  */
@Entity
@Table(name = "grapes")
@Setter
@Getter
@NoArgsConstructor
@ToString
public class Grapes {

    /** Unique identifier */
    @Id
    @Column(name = "grapeId")
    private UUID grapeID = UUID.randomUUID();

    /** Name of grape type*/
    @Column(name = "grapeName")
    private String grapeName;

    /**  The entity instance creation. */
    public Grapes(String grapeName) {
        this.grapeName = grapeName;
    }
}


