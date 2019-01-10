package com.ote.mandate.business.event.api;

import com.ote.mandate.business.event.model.MandateHeirRemovedEvent;
import reactor.core.publisher.Mono;

public interface IMandateHeirRemovedEventHandler {

    Mono<Boolean> onMandateHeirRemovedEvent(Mono<MandateHeirRemovedEvent> event);
}
