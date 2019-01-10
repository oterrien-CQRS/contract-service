package com.ote.mandate.business.event.api;

import com.ote.mandate.business.event.model.MandateMainHeirDefinedEvent;
import reactor.core.publisher.Mono;

public interface IMandateMainHeirDefinedEventHandler {

    Mono<Boolean> onMandateMainHeirDefinedEvent(Mono<MandateMainHeirDefinedEvent> event);
}
