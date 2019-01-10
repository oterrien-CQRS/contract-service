package com.ote.mandate.business.event.business;

import com.ote.mandate.business.event.api.IMandateEventHandler;
import com.ote.mandate.business.event.model.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@NoArgsConstructor(access = AccessLevel.PACKAGE)
@Slf4j
class MandateEventHandler implements IMandateEventHandler {

    @Override
    public Mono<Boolean> onMandateCreatedEvent(Mono<MandateCreatedEvent> event) {
        return event.map(p -> {
            log.warn("Message received : " + p.toString());
            return true;
        });
    }

    @Override
    public Mono<Boolean> onMandateHeirAddedEvent(Mono<MandateHeirAddedEvent> event) {
        return event.map(p -> {
            log.warn("Message received : " + p.toString());
            return true;
        });
    }

    @Override
    public Mono<Boolean> onMandateHeirRemovedEvent(Mono<MandateHeirRemovedEvent> event) {
        return event.map(p -> {
            log.warn("Message received : " + p.toString());
            return true;
        });
    }

    @Override
    public Mono<Boolean> onMandateMainHeirDefinedEvent(Mono<MandateMainHeirDefinedEvent> event) {
        return event.map(p -> {
            log.warn("Message received : " + p.toString());
            return true;
        });
    }

    @Override
    public Mono<Boolean> onMandateNotaryDefinedEvent(Mono<MandateNotaryDefinedEvent> event) {
        return event.map(p -> {
            log.warn("Message received : " + p.toString());
            return true;
        });
    }
}
