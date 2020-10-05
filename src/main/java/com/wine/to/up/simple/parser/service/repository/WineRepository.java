package com.wine.to.up.simple.parser.service.repository;

import com.wine.to.up.simple.parser.service.domain.entity.Wine;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface WineRepository extends CrudRepository<Wine, UUID> {
    Wine findWineByName(String name);

    @Query("SELECT w.wineID FROM Wine w WHERE w.name=?1")
    UUID getIdByName(String name);
}
