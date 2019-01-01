package com.ote.mandate.service.event;

import com.ote.mandate.service.event.model.EventDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface EventRepository extends MongoRepository<EventDocument, String> {

    @Query(value = "{ 'event.event_id' : ?0 }")
    List<EventDocument> findAllByEventId(String id);
}
