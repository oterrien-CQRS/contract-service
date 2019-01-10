package com.ote.mandate.service.mock;

import com.ote.common.JsonUtils;
import com.ote.common.cqrs.IEvent;
import com.ote.mandate.business.event.api.IMandateEventHandler;
import com.ote.mandate.business.event.api.MandateEventHandlerProvider;
import com.ote.mandate.business.event.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.io.IOException;

@Service
@EnableBinding(Sink.class)
@Slf4j
public class KafkaProcessorMock implements IMandateEventHandler {

    private final IMandateEventHandler eventHandler;

    private final Sink sink;

    public KafkaProcessorMock(@Autowired Sink sink) {
        this.sink = sink;
        this.eventHandler = MandateEventHandlerProvider.getInstance().getHandlerFactory().createEventHandler();
    }

    @StreamListener(value = Sink.INPUT)
    public void consume(Message<String> message) throws Exception {
        String messageContent = message.getPayload();
        Object eventType = message.getHeaders().get("EventType");
        if (eventType != null) {
            Class eventClass = Class.forName(eventType.toString());
            IEvent event = parse(messageContent, eventClass);

            if (event instanceof MandateCreatedEvent) {
                onMandateCreatedEvent(Mono.just((MandateCreatedEvent) event)).subscribeOn(Schedulers.elastic());
            } else if (event instanceof MandateHeirAddedEvent) {
                onMandateHeirAddedEvent(Mono.just((MandateHeirAddedEvent) event)).subscribeOn(Schedulers.elastic());
            } else if (event instanceof MandateHeirRemovedEvent) {
                onMandateHeirRemovedEvent(Mono.just((MandateHeirRemovedEvent) event)).subscribeOn(Schedulers.elastic());
            } else if (event instanceof MandateMainHeirDefinedEvent) {
                onMandateMainHeirDefinedEvent(Mono.just((MandateMainHeirDefinedEvent) event)).subscribeOn(Schedulers.elastic());
            } else if (event instanceof MandateNotaryDefinedEvent) {
                onMandateNotaryDefinedEvent(Mono.just((MandateNotaryDefinedEvent) event)).subscribeOn(Schedulers.elastic());
            }

        } else {
            log.warn("Received <-- " + messageContent);
        }
    }

    @SuppressWarnings("unchecked")
    private IEvent parse(String jsonValue, Class eventClass) throws IOException {
        return JsonUtils.parse(jsonValue, (Class<IEvent>) eventClass);
    }

    @Override
    public Mono<Boolean> onMandateCreatedEvent(Mono<MandateCreatedEvent> event) {
        return eventHandler.onMandateCreatedEvent(event);
    }

    @Override
    public Mono<Boolean> onMandateHeirAddedEvent(Mono<MandateHeirAddedEvent> event) {
        return eventHandler.onMandateHeirAddedEvent(event);
    }

    @Override
    public Mono<Boolean> onMandateHeirRemovedEvent(Mono<MandateHeirRemovedEvent> event) {
        return eventHandler.onMandateHeirRemovedEvent(event);
    }

    @Override
    public Mono<Boolean> onMandateMainHeirDefinedEvent(Mono<MandateMainHeirDefinedEvent> event) {
        return eventHandler.onMandateMainHeirDefinedEvent(event);
    }

    @Override
    public Mono<Boolean> onMandateNotaryDefinedEvent(Mono<MandateNotaryDefinedEvent> event) {
        return eventHandler.onMandateNotaryDefinedEvent(event);
    }
}