package com.wine.to.up.simple.parser.service.repository;

import com.wine.to.up.simple.parser.service.domain.entity.Wine;
import org.apache.kafka.common.protocol.types.Field;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface WineRepository extends CrudRepository<Wine, UUID> {
    Wine findWineByName(String name);
    Wine findWineByNameAndPriceAndVolumeAndColorTypeAndSugarType(String name, Float price, Float volume, String ColorType, String SugarType);
    Wine findWineByLinkAndPrice(String link, float price);

    @Query("SELECT w.wineID FROM Wine w WHERE w.name=?1")
    UUID getIdByName(String name);

    Boolean existsWineByLinkAndPrice(String link, float price);
    Boolean existsWineByNameAndPriceAndYear(String name, float price, int year);
    Boolean existsWineByNameAndPriceAndVolumeAndColorTypeAndSugarTypeAndYear(String name, Float price, Float volume, String ColorType, String SugarType, int year);
}
