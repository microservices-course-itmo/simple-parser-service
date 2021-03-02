package com.wine.to.up.simple.parser.service.messaging.serialization;

import com.wine.to.up.demo.service.api.message.KafkaMessageSentEventOuterClass.KafkaMessageSentEvent;
import com.wine.to.up.parser.common.api.schema.ParserApi;
import org.apache.kafka.common.serialization.Serializer;

/**
 * Serializer for {@link KafkaMessageSentEvent}
 */
public class EventSerializer implements Serializer<ParserApi.WineParsedEvent> {
    /**
     * {@inheritDoc}
     */
    @Override
    public byte[] serialize(String topic, ParserApi.WineParsedEvent data) {
        return data.toByteArray();
    }
}
