/**
 * com.wine.to.up.simple.parser.service.messaging is a package for handling messages from kafka.
 */
package com.wine.to.up.simple.parser.service.messaging;

import com.wine.to.up.commonlib.messaging.KafkaMessageHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import java.util.concurrent.atomic.AtomicInteger;
/**
 * Counting number of messages from Kafka (for String messages).
 */
@Component
@Slf4j
public class SmopikTopicKafkaMessageHandler implements KafkaMessageHandler<String> {
    /** Counter of messages */
    private final AtomicInteger counter = new AtomicInteger(0);
    /**
     * Function for getting {@link SmopikTopicKafkaMessageHandler#counter}
     */
    @Override
    public void handle(String message) {
        counter.incrementAndGet();
        log.info("Message received message of type {}, number of messages: {}", message.getClass().getSimpleName(),
                counter.get());
    }
}