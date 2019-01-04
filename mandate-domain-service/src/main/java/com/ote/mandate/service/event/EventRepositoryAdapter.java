package com.ote.mandate.service.event;

import com.ote.framework.IEvent;
import com.ote.mandate.business.spi.IEventRepository;
import com.ote.mandate.service.event.model.EventDocument;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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
    public void storeAndPublish(IEvent event) {
        log.debug("Storing and publishing event " + event.getClass().getTypeName());
        try {
            EventDocument document = new EventDocument();
            document.setCreatedTime(LocalDateTime.now());
            document.setEvent(this.mandateEventMapperService.getEventToDocument().convert(event));
            eventRepository.save(document);
            log.debug("Event {} has been stored", event.getClass().getSimpleName());
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            eventPublisherService.publish(event);
            log.debug("Event {} has been published", event.getClass().getSimpleName());
        }
    }


    @Override
    public List<IEvent> findAll(String id) {
        return eventRepository.findAllByEventId(id).
                stream().
                map(document -> document.getEvent()).
                map(event -> this.mandateEventMapperService.getDocumentToEvent().convert(event)).
                collect(Collectors.toList());
    }
}
