package com.wine.to.up.simple.parser.service.domain.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.FileWriter;
import java.util.UUID;

@Entity
@Table(name = "countries")
@Setter
@Getter
@NoArgsConstructor
@ToString
public class Countries {
    @Id
    @Column(name = "countryid")
    private UUID countryID = UUID.randomUUID();

    @Column(name = "countryname")
    private String countryName;

    public Countries(String countryName) {
        this.countryName = countryName;
    }

    @SneakyThrows
    public void writeInfoToFile(Countries someCountry){
        FileWriter writer = new FileWriter("Countries.txt", false);
        writer.write(someCountry.toString() + "\n");
        writer.flush();
    }
}

