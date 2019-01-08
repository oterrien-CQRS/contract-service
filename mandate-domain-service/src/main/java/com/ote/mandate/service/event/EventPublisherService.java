package com.ote.mandate.service.event;

import com.ote.framework.IEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@EnableBinding(Source.class)
@Slf4j
public class EventPublisherService {

    private final Source source;

    public EventPublisherService(@Autowired Source source) {
        this.source = source;
    }

    public Mono<Boolean> publish(Mono<IEvent> event) {
        return event.
                doOnNext(evt -> log.debug("Trying to publish event message {}", evt)).
                map(evt -> MessageBuilder.withPayload(evt).setHeader("EventType", evt.getClass().getName()).build()).
                map(msg -> source.output().send(msg));
    }

    public void publish(IEvent event) {

        log.debug("Trying to publish event message {}", event);
        Message<IEvent> message = MessageBuilder.withPayload(event).setHeader("EventType", event.getClass().getName()).build();
        source.output().send(message);
    }
}
