package com.wine.to.up.simple.parser.service.simple_parser;

import com.wine.to.up.parser.common.api.schema.ParserApi;
import com.wine.to.up.simple.parser.service.simple_parser.mappers.WineMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * The class designed for wrapping wine parsed information into the common-api ParserApi.WineParsedEvent class.
 * The Proto3 protocol is used to transfer data to Kafka.
 */
@Service
@Slf4j
public class WineToDTO {

    private WineMapper wineMapper;

    @Autowired
    public WineToDTO(WineMapper modelMapper) {
        wineMapper = modelMapper;
    }

    /**
     * The function is designed to get parsed wine information which is wrapped into a common-api ParserApi.WineParsedEvent class for subsequent transfer to Kafka.
     *
     * @param wine instance of the SimpleWine class which contains parsed wine information.
     * @return instance of ParserApi.WineParsedEvent
     **/
    public ParserApi.Wine getProtoWine(SimpleWine wine) {
        ParserApi.Wine.Builder product = wineMapper.toKafka(wine);
        return product.build();
    }
}
