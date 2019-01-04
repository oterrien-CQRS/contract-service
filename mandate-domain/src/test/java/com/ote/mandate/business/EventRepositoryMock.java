package com.ote.mandate.business;

import com.ote.framework.IEvent;
import com.ote.mandate.business.spi.IEventRepository;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class EventRepositoryMock implements IEventRepository {

    private Map<Object, List<IEvent>> eventStore = new HashMap<>();

    @Override
    public Mono<Boolean> storeAndPublish(Mono<IEvent> event) {

        return event
                .map(evt -> {
                    log.info("Storing and publishing event {}", evt.getClass().getTypeName());
                    eventStore.computeIfAbsent(evt.getId(), p -> new ArrayList<>()).add(evt);
                    return true;
                })
                .onErrorReturn(false);
    }

    @Override
    public Flux<IEvent> findAll(Mono<String> id) {
        return id.map(p -> eventStore.get(p)).flatMapIterable(ArrayList::new);
    }

    public void clean() {
        eventStore.clear();
    }
}
