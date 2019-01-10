package com.ote.mandate.service.event;

import com.ote.common.cqrs.IEvent;
import com.ote.mandate.business.command.spi.IEventRepository;
import com.ote.mandate.service.event.model.EventDocument;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
@Slf4j
public class EventRepositoryAdapter implements IEventRepository {

    private final EventRepository eventRepository;
    private final EventPublisherService eventPublisherService;
    private final MandateEventMapperService mandateEventMapperService;

    public EventRepositoryAdapter(@Autowired EventRepository eventRepository,
                                  @Autowired EventPublisherService eventPublisherService,
                                  @Autowired MandateEventMapperService mandateEventMapperService) {

        this.eventRepository = eventRepository;
        this.eventPublisherService = eventPublisherService;
        this.mandateEventMapperService = mandateEventMapperService;
    }

    @Override
    public Mono<Boolean> storeAndPublish(Mono<IEvent> monoEvent) {

        return monoEvent.
                doOnNext(evt -> log.debug("Storing and publishing event " + evt.getClass().getTypeName())).
                flatMap(evt -> {
                    EventDocument document = new EventDocument();
                    document.setCreatedTime(LocalDateTime.now());
                    document.setEvent(this.mandateEventMapperService.getEventToDocument().convert(evt));
                    return eventRepository.save(document).
                            doOnSuccess(doc -> log.debug("Event {} has been stored", doc.getClass().getSimpleName())).
                            map(doc -> evt);
                }).
                flatMap(evt -> eventPublisherService.publish(Mono.just(evt)).
                        doOnSuccess(bool -> log.debug("Event {} has been pushed", evt.getClass().getSimpleName()))
                ).
                onErrorReturn(false);
    }


    @Override
    public Flux<IEvent> findAll(Mono<String> id) {

        return eventRepository.findAllByEventId(id).map(EventDocument::getEvent).
                map(event -> this.mandateEventMapperService.getDocumentToEvent().convert(event));
    }
}
