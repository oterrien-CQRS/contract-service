package com.ote.mandate.service.event;

import com.ote.framework.IEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;

@Service
@EnableBinding(Source.class)
@Slf4j
public class EventPublisherService {

    private final Source source;

    public EventPublisherService(@Autowired Source source) {
        this.source = source;
    }

    public void publish(IEvent event) {
        Message<IEvent> message = MessageBuilder.withPayload(event).build();
        source.output().send(message);
    }
}
