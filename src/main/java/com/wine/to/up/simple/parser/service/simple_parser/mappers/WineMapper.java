package com.wine.to.up.simple.parser.service.simple_parser.mappers;

import com.wine.to.up.parser.common.api.schema.ParserApi;
import com.wine.to.up.simple.parser.service.domain.entity.Wine;
import com.wine.to.up.simple.parser.service.simple_parser.SimpleWine;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WineMapper {

    private ModelMapper modelMapper;

    @Autowired
    public WineMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }


    public ParserApi.Wine.Builder toKafka(SimpleWine wine) {

        //The final type, which is written in protobuf3, cannot be manually configured in beans. The converter doesn't help.
        return modelMapper.map(wine, ParserApi.Wine.Builder.class)
                .addRegion(wine.getRegion())
                .setColor(wine.getColor())
                .setSugar(wine.getSugar())
                .addAllGrapeSort(wine.getGrapeSort());
    }

    public Wine toDB(SimpleWine wine) {
        return wine == null ? null : modelMapper.map(wine, Wine.class);
    }
}