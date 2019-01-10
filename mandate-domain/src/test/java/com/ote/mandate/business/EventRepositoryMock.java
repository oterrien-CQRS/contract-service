package com.ote.mandate.business;

import com.ote.common.cqrs.IEvent;
import com.ote.mandate.business.command.spi.IEventRepository;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@Slf4j
public class EventRepositoryMock implements IEventRepository {

    private final Map<String, List<IEvent>> eventStore = new HashMap<>();

    @Override
    public Mono<Boolean> storeAndPublish(Mono<IEvent> event) {

        return event.
                doOnNext(evt -> log.debug("#### MOCK - Storing and publishing event {}", evt.toString()))
                .map(evt -> eventStore.computeIfAbsent(evt.getId(), p -> new ArrayList<>()).add(evt))
                .onErrorReturn(false);
    }

    @Override
    public Flux<IEvent> findAll(Mono<String> id) {
        return id.
                map(i -> eventStore.getOrDefault(i, new ArrayList<>())).
                flatMapIterable(ArrayList::new);
    }

    public void initWith(IEvent... events) {

        Stream.of(events).forEach(evt -> eventStore.computeIfAbsent(evt.getId(), p -> new ArrayList<>()).add(evt));
    }

    public void clean() {
        eventStore.clear();
    }
}
