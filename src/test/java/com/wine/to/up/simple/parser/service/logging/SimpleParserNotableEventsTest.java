package com.wine.to.up.simple.parser.service.logging;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.Assert.*;

class SimpleParserNotableEventsTest {
    @ParameterizedTest
    @CsvSource({"I_CONTROLLER_RECEIVED_MESSAGE,I_CONTROLLER_RECEIVED_MESSAGE",
            "I_KAFKA_SEND_MESSAGE_SUCCESS,I_KAFKA_SEND_MESSAGE_SUCCESS",
            "W_SOME_WARN_EVENT,W_SOME_WARN_EVENT"})
    void testGetName(String expectedName, SimpleParserNotableEvents event) {
        assertEquals(expectedName, event.getName());
    }

    @ParameterizedTest
    @CsvSource({"Message: {},I_CONTROLLER_RECEIVED_MESSAGE",
            "Kafka send message: {},I_KAFKA_SEND_MESSAGE_SUCCESS",
            "Warn situation. Description: {},W_SOME_WARN_EVENT"})
    void testGetTemplate(String expectedTemplate, SimpleParserNotableEvents event) {
        assertEquals(expectedTemplate, event.getTemplate());
    }
}
