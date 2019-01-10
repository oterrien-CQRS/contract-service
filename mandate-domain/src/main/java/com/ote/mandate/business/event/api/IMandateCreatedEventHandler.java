package com.ote.mandate.business.event.api;

import com.ote.mandate.business.event.model.MandateCreatedEvent;
import reactor.core.publisher.Mono;

public interface IMandateCreatedEventHandler {

    Mono<Boolean> onMandateCreatedEvent(Mono<MandateCreatedEvent> event);
}
