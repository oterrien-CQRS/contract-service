package com.ote.mandate.business.event.api;

import com.ote.mandate.business.event.model.MandateNotaryDefinedEvent;
import reactor.core.publisher.Mono;

public interface IMandateNotaryDefinedEventHandler {

    Mono<Boolean> onMandateNotaryDefinedEvent(Mono<MandateNotaryDefinedEvent> event);
}
