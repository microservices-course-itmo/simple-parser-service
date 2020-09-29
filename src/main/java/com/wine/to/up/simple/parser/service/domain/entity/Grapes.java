package com.wine.to.up.simple.parser.service.domain.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.FileWriter;
import java.util.UUID;

@Entity
@Table(name = "grapes")
@Setter
@Getter
@NoArgsConstructor
@ToString
public class Grapes {
    @Id
    @Column(name = "grapeid")
    private UUID grapeID = UUID.randomUUID();

    @Column(name = "grapename")
    private String grapeName;

    public Grapes(String grapeName) {
        this.grapeName = grapeName;
    }

    @SneakyThrows
    public void writeInfoToFile(Grapes someGrape){
        FileWriter writer = new FileWriter("Grapes.txt", false);
        writer.write(someGrape.toString() + "\n");
        writer.flush();
    }
}


