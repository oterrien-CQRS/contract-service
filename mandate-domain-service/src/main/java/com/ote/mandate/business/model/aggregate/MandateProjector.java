package com.ote.mandate.business.model.aggregate;

import com.ote.framework.EventHandlers;
import com.ote.framework.IEvent;
import com.ote.framework.IProjector;
import com.ote.mandate.business.exception.MandateAlreadyCreatedException;
import com.ote.mandate.business.exception.MandateNotYetCreatedException;
import com.ote.mandate.business.model.event.*;

import java.util.List;

public final class MandateProjector implements IProjector<Mandate> {

    private final EventHandlers eventHandlers = new EventHandlers();

    private Mandate mandate;

    public MandateProjector() {
        eventHandlers.bind(MandateCreatedEvent.class, this::handle);
        eventHandlers.bind(MandateHeirAddedEvent.class, this::handle);
        eventHandlers.bind(MandateHeirRemovedEvent.class, this::handle);
        eventHandlers.bind(MandateMainHeirDefinedEvent.class, this::handle);
        eventHandlers.bind(MandateNotaryDefinedEvent.class, this::handle);
    }

    @Override
    public Mandate project(List<IEvent> events) throws Exception {

        mandate = null;
        eventHandlers.handle(events);
        return mandate;
    }

    private void handle(MandateCreatedEvent event) throws MandateAlreadyCreatedException {

        if (mandate != null) {
            throw new MandateAlreadyCreatedException(event.getId());
        }

        mandate = new Mandate(event.getId());
        mandate.setBankName(event.getBankName());
        mandate.setContractor(event.getContractor());
        mandate.setNotary(event.getNotary());
        mandate.setMainHeir(event.getMainHeir());
        mandate.getOtherHeirs().addAll(event.getOtherHeirs());
    }

    private void handle(MandateHeirAddedEvent event) throws MandateNotYetCreatedException {

        if (mandate == null) {
            throw new MandateNotYetCreatedException(event.getId());
        }

        mandate.getOtherHeirs().add(event.getHeir());
    }

    private void handle(MandateHeirRemovedEvent event) throws MandateNotYetCreatedException {

        if (mandate == null) {
            throw new MandateNotYetCreatedException(event.getId());
        }

        mandate.getOtherHeirs().remove(event.getHeir());
    }

    private void handle(MandateMainHeirDefinedEvent event) throws MandateNotYetCreatedException {

        if (mandate == null) {
            throw new MandateNotYetCreatedException(event.getId());
        }

        mandate.setMainHeir(event.getHeir());
    }

    private void handle(MandateNotaryDefinedEvent event) throws MandateNotYetCreatedException {

        if (mandate == null) {
            throw new MandateNotYetCreatedException(event.getId());
        }

        mandate.setNotary(event.getNotary());
    }
}
