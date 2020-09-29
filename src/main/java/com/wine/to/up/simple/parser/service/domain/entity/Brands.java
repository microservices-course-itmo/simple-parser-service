package com.wine.to.up.simple.parser.service.domain.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.FileWriter;
import java.util.UUID;

@Entity
@Table(name = "brands")
@Setter
@Getter
@NoArgsConstructor
@ToString
public class Brands {
    @Id
    @Column(name = "brandid")
    private UUID brandID = UUID.randomUUID();

    @Column(name = "brandname")
    private String brandName;

    public Brands(String grapeName) {
        this.brandName = grapeName;
    }

    @SneakyThrows
    public void writeInfoToFile(Brands someBrand) {
        FileWriter writer = new FileWriter("Brands.txt", false);
        writer.write(someBrand.toString() + "\n");
        writer.flush();
    }

}
