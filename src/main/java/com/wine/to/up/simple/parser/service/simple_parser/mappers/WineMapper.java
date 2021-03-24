package com.wine.to.up.simple.parser.service.simple_parser.mappers;

import com.wine.to.up.parser.common.api.schema.ParserApi;
import com.wine.to.up.simple.parser.service.domain.entity.Wine;
import com.wine.to.up.simple.parser.service.simple_parser.SimpleWine;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class WineMapper {

    private final ModelMapper modelMapper;

    public WineMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    /**
     * @param wine - {@link SimpleWine} class instance
     * @return New wine
     */
    public ParserApi.Wine.Builder toKafka(SimpleWine wine) {

        //The final type, which is written in protobuf3, cannot be manually configured in beans. The converter doesn't help.
        ParserApi.Wine.Builder newWine = modelMapper.map(wine, ParserApi.Wine.Builder.class)
                .addRegion(wine.getRegion());

        if (wine.getGrapeSort() != null) {
            newWine.addAllGrapeSort(wine.getGrapeSort());
        }

        return newWine;
    }

    public ParserApi.Wine.Builder toKafka(Wine wine) {
        return modelMapper.map(wine, ParserApi.Wine.Builder.class)
                .addRegion(wine.getRegion());
    }

    public Wine toEntity(SimpleWine wine) {
        return wine == null ? null : modelMapper.map(wine, Wine.class);
    }
}