package com.wine.to.up.simple.parser.service.logging;

import com.wine.to.up.commonlib.logging.NotableEvent;

public enum SimpleParserNotableEvents implements NotableEvent {
    I_KAFKA_SEND_MESSAGE_SUCCESS("Kafka send message: {}"), I_CONTROLLER_RECEIVED_MESSAGE("Message: {}"),
    W_SOME_WARN_EVENT("Warn situation. Description: {}");

    private final String template;

    SimpleParserNotableEvents(String template) {
        this.template = template;
    }

    public String getTemplate() {
        return template;
    }

    @Override
    public String getName() {
        return name();
    }

}
