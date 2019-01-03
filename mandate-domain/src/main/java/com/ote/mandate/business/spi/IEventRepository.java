package com.ote.mandate.business.spi;

import com.ote.framework.IEvent;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IEventRepository {

    Mono<Boolean> storeAndPublish(Mono<IEvent> event);

    Flux<IEvent> findAll(String id);
}

