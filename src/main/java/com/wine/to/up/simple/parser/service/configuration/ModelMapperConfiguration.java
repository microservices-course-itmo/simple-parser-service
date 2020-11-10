package com.wine.to.up.simple.parser.service.configuration;

import com.wine.to.up.parser.common.api.schema.ParserApi;
import com.wine.to.up.simple.parser.service.domain.entity.Wine;
import com.wine.to.up.simple.parser.service.simple_parser.SimpleWine;
import org.modelmapper.*;
import org.modelmapper.spi.MappingContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.Map;

import static com.wine.to.up.parser.common.api.schema.ParserApi.Wine.Color.*;
import static com.wine.to.up.parser.common.api.schema.ParserApi.Wine.Sugar.*;
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

        Map<ParserApi.Wine.Color, String> colorMap = Map.of(RED, "красное", ROSE, "розовое", WHITE, "белое", ORANGE, "оранжевое", ParserApi.Wine.Color.UNRECOGNIZED, "");
        Map<ParserApi.Wine.Sugar, String> sugarMap = Map.of(DRY, "сухое", MEDIUM_DRY, "полусухое", MEDIUM, "полусладкое", SWEET, "сладкое", ParserApi.Wine.Sugar.UNRECOGNIZED, "");
        Converter<ParserApi.Wine.Color, String> colorToString = ctx -> ctx.getSource() == null ? null : colorMap.getOrDefault(ctx.getSource(), "");
        Converter<ParserApi.Wine.Sugar, String> sugarToString = ctx -> ctx.getSource() == null ? null : sugarMap.getOrDefault(ctx.getSource(), "");

        PropertyMap<SimpleWine, Wine> propertyMapDB = new PropertyMap<>() {
            protected void configure() {
                skip().setWineID(null);
                using(sugarToString).map(source.getSugar()).setSugar(null);
                using(colorToString).map(source.getColor()).setColor(null);
                map().setBrandID(source.getBrandID());
                map().setCountryID(source.getCountryID());
                map().setGrapeSort(String.valueOf(source.getGrapeSort()));
            }
        };

        modelMapper.addMappings(propertyMapDB);
    }
}

