package com.ote.mandate.business.event.api;

import com.ote.mandate.business.event.model.MandateHeirAddedEvent;
import reactor.core.publisher.Mono;

public interface IMandateHeirAddedEventHandler {

    Mono<Boolean> onMandateHeirAddedEvent(Mono<MandateHeirAddedEvent> event);
}
