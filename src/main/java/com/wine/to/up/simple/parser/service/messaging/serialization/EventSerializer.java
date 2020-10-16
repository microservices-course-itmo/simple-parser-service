package com.wine.to.up.simple.parser.service.messaging.serialization;

import com.wine.to.up.demo.service.api.message.KafkaMessageSentEventOuterClass.KafkaMessageSentEvent;
import com.wine.to.up.parser.common.api.schema.UpdateProducts;
import org.apache.kafka.common.serialization.Serializer;

/**
 * Serializer for {@link KafkaMessageSentEvent}
 */
public class EventSerializer implements Serializer<UpdateProducts.UpdateProductsMessage> {
    /**
     * {@inheritDoc}
     */
    @Override
    public byte[] serialize(String topic, UpdateProducts.UpdateProductsMessage data) {
        return data.toByteArray();
    }
}
