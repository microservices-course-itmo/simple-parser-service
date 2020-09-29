package com.wine.to.up.simple.parser.service.repository;

import com.wine.to.up.simple.parser.service.domain.entity.Countries;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface CountriesRepository extends CrudRepository<Countries, UUID> {

}