package com.ote.mandate.business.command.spi;

import com.ote.common.cqrs.IEvent;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IEventRepository {

    Mono<Boolean> storeAndPublish(Mono<IEvent> event);

    Flux<IEvent> findAll(Mono<String> id);
}
