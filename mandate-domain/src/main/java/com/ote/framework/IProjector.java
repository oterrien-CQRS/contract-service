package com.ote.framework;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IProjector<T> extends AutoCloseable {

    Mono<T> project(Flux<IEvent> events);
}
