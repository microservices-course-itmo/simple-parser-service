/**
 * com.wine.to.up.simple.parser.service.messaging is a package for handling messages from kafka.
 */
package com.wine.to.up.simple.parser.service.messaging;

import com.wine.to.up.commonlib.messaging.KafkaMessageHandler;
import com.wine.to.up.parser.common.api.schema.ParserApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import java.util.concurrent.atomic.AtomicInteger;
/**
 * Counting number of messages from Kafka (about adding new Product).
 */
@Component
@Slf4j
public class TestTopicKafkaMessageHandler implements KafkaMessageHandler<ParserApi.WineParsedEvent> {
    /** Counter of messages */
    private final AtomicInteger counter = new AtomicInteger(0);
    /**
     * Function for getting {@link TestTopicKafkaMessageHandler#counter}
     */
    @Override
    public void handle(ParserApi.WineParsedEvent message) {
        counter.incrementAndGet();
        log.debug("Message received message of type {}, number of messages: {}", message.getClass().getSimpleName(),
                counter.get());
    }
}