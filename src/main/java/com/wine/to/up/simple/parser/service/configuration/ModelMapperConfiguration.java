package com.wine.to.up.simple.parser.service.configuration;

import com.wine.to.up.parser.common.api.schema.ParserApi;
import com.wine.to.up.simple.parser.service.domain.entity.Wine;
import com.wine.to.up.simple.parser.service.simple_parser.SimpleWine;
import com.wine.to.up.simple.parser.service.simple_parser.enums.Color;
import com.wine.to.up.simple.parser.service.simple_parser.enums.Sugar;
import org.modelmapper.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

import static org.modelmapper.convention.MatchingStrategies.STRICT;


@Configuration
public class ModelMapperConfiguration {

    @Bean
    public ModelMapper modelMapper() {

        ModelMapper modelMapper = new ModelMapper();
        modelMapper
                .getConfiguration()
                .setSkipNullEnabled(true)
                .setFieldMatchingEnabled(true)
                .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE)
                .setMatchingStrategy(STRICT)
                .setAmbiguityIgnored(true);

        return modelMapper;
    }

    @PostConstruct
    public void setup() {

        ModelMapper modelMapper = this.modelMapper();

        modelMapper.createTypeMap(SimpleWine.class, ParserApi.Wine.Builder.class);

        Converter<ParserApi.Wine.Color, String> colorToString = ctx -> ctx.getSource() == null ? "нет информации" : Color.getStringColor(ctx.getSource());
        Converter<ParserApi.Wine.Sugar, String> sugarToString = ctx -> ctx.getSource() == null ? "нет информации" : Sugar.getStringSugar(ctx.getSource());

        PropertyMap<SimpleWine, Wine> propertyMapDB = new PropertyMap<>() {
            protected void configure() {
                skip().setWineID(null);
                using(sugarToString).map(source.getSugar()).setSugar("");
                using(colorToString).map(source.getColor()).setColor("");
                map().setBrandID(source.getBrandID());
                map().setCountryID(source.getCountryID());
                map().setGrapeSort(String.valueOf(source.getGrapeSort()));
            }
        };

        modelMapper.addMappings(propertyMapDB);
    }
}

