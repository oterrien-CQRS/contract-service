package com.ote.mandate.service.event;

import com.ote.common.Convertor;
import com.ote.common.cqrs.IEvent;
import com.ote.mandate.business.aggregate.Mandate;
import com.ote.mandate.business.event.model.*;
import com.ote.mandate.service.event.model.*;
import lombok.Getter;
import org.springframework.stereotype.Service;

@Service
@Getter
public class MandateEventMapperService {

    private EventToDocument eventToDocument = new EventToDocument();
    private DocumentToEvent documentToEvent = new DocumentToEvent();

    public static class EventToDocument {

        private final Convertor convertor = new Convertor();

        public EventToDocument() {
            this.convertor.bind(MandateCreatedEvent.class, this::convert);
            this.convertor.bind(MandateHeirAddedEvent.class, this::convert);
            this.convertor.bind(MandateHeirRemovedEvent.class, this::convert);
            this.convertor.bind(MandateMainHeirDefinedEvent.class, this::convert);
            this.convertor.bind(MandateNotaryDefinedEvent.class, this::convert);
        }

        public InnerEventDocument convert(IEvent event) {
            return this.convertor.convert(event);
        }

        private MandateCreatedEventDocument convert(MandateCreatedEvent event) {

            Mandate mandate = new Mandate();
            mandate.setId(event.getId());
            mandate.setBankName(event.getBankName());
            mandate.setContractor(event.getContractor());
            mandate.setNotary(event.getNotary());
            mandate.setMainHeir(event.getMainHeir());
            mandate.setOtherHeirs(event.getOtherHeirs());

            MandateCreatedEventDocument document = new MandateCreatedEventDocument();
            document.setId(event.getId());
            document.setMandate(mandate);
            return document;
        }

        private MandateOtherHeirUpdatedEventDocument convert(MandateHeirAddedEvent event) {
            MandateOtherHeirUpdatedEventDocument document = new MandateOtherHeirUpdatedEventDocument();
            document.setId(event.getId());
            document.setHeir(event.getHeir());
            document.setAction(MandateOtherHeirUpdatedEventDocument.Action.ADDED);
            return document;
        }

        private MandateOtherHeirUpdatedEventDocument convert(MandateHeirRemovedEvent event) {
            MandateOtherHeirUpdatedEventDocument document = new MandateOtherHeirUpdatedEventDocument();
            document.setId(event.getId());
            document.setHeir(event.getHeir());
            document.setAction(MandateOtherHeirUpdatedEventDocument.Action.DELETED);
            return document;
        }

        private MandateMainHeirDefinedEventDocument convert(MandateMainHeirDefinedEvent event) {
            MandateMainHeirDefinedEventDocument document = new MandateMainHeirDefinedEventDocument();
            document.setId(event.getId());
            document.setHeir(event.getHeir());
            return document;
        }

        private MandateNotaryDefinedEventDocument convert(MandateNotaryDefinedEvent event) {
            MandateNotaryDefinedEventDocument document = new MandateNotaryDefinedEventDocument();
            document.setId(event.getId());
            document.setNotary(event.getNotary());
            return document;
        }
    }

    public static class DocumentToEvent {

        private final Convertor convertor = new Convertor();

        public DocumentToEvent() {
            this.convertor.bind(MandateCreatedEventDocument.class, this::convert);
            this.convertor.bind(MandateOtherHeirUpdatedEventDocument.class, this::convert);
            this.convertor.bind(MandateMainHeirDefinedEventDocument.class, this::convert);
            this.convertor.bind(MandateNotaryDefinedEventDocument.class, this::convert);
        }

        public <T extends InnerEventDocument> IEvent convert(T event) {
            return this.convertor.convert(event);
        }

        public IEvent convert(MandateCreatedEventDocument document) {

            MandateCreatedEvent event = new MandateCreatedEvent(document.getId(), document.getMandate().getBankName(), document.getMandate().getContractor());
            event.setMainHeir(document.getMandate().getMainHeir());
            event.setNotary(document.getMandate().getNotary());
            event.getOtherHeirs().addAll(document.getMandate().getOtherHeirs());
            return event;
        }

        public IEvent convert(MandateOtherHeirUpdatedEventDocument document) {

            switch (document.getAction()) {
                case ADDED: {
                    MandateHeirAddedEvent event = new MandateHeirAddedEvent(document.getId(), document.getHeir());
                    return event;
                }
                default: {
                    MandateHeirRemovedEvent event = new MandateHeirRemovedEvent(document.getId(), document.getHeir());
                    return event;
                }
            }
        }

        public IEvent convert(MandateMainHeirDefinedEventDocument document) {

            MandateMainHeirDefinedEvent event = new MandateMainHeirDefinedEvent(document.getId(), document.getHeir());
            return event;
        }

        public IEvent convert(MandateNotaryDefinedEventDocument document) {
            MandateNotaryDefinedEvent event = new MandateNotaryDefinedEvent(document.getId(), document.getNotary());
            return event;
        }
    }


}
