package com.wine.to.up.simple.parser.service.domain.entity;

import lombok.*;

import javax.persistence.*;
import java.io.FileWriter;
import java.util.UUID;

@Entity
@Table(name = "winegrapesinfo")
@Setter
@Getter
@ToString
public class WineGrapesInfo {
    @Id
    @Column(name = "id")
    private UUID ID = UUID.randomUUID();

    @Column(name = "winegrapesid")
    private UUID wineGrapesID;

    @OneToOne
    @JoinColumn(name = "grapeid", referencedColumnName = "grapeid")
    private Grapes grapeID;

    public WineGrapesInfo(){
        this.wineGrapesID = UUID.randomUUID();
    }
    public WineGrapesInfo(Grapes grapeID) {
        this.grapeID = grapeID;
    }

    @SneakyThrows
    public void writeInfoToFile(WineGrapesInfo wineGrapesInfo){
        FileWriter writer = new FileWriter("WineGrapesInfo.txt", false);
        writer.write(wineGrapesInfo.toString() + "\n");
        writer.flush();
    }

}
