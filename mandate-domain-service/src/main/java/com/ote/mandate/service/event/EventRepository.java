package com.ote.mandate.service.event;

import com.ote.mandate.service.event.model.EventDocument;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface EventRepository extends ReactiveMongoRepository<EventDocument, String> {

    @Query(value = "{ 'event.event_id' : ?0 }")
    Flux<EventDocument> findAllByEventId(Mono<String> id);
}
