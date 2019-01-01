package com.ote.mandate.business.domain;

import com.ote.framework.IEvent;
import com.ote.mandate.business.api.IMandateCommandService;
import com.ote.mandate.business.exception.MandateAlreadyCreatedException;
import com.ote.mandate.business.exception.MandateNotYetCreatedException;
import com.ote.mandate.business.model.aggregate.Mandate;
import com.ote.mandate.business.model.aggregate.MandateProjector;
import com.ote.mandate.business.model.command.*;
import com.ote.mandate.business.model.event.*;
import com.ote.mandate.business.spi.IEventRepository;

import java.util.List;
import java.util.Objects;

class MandateCommandService implements IMandateCommandService {

    private final IEventRepository eventRepository;

    MandateCommandService(IEventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Override
    public void apply(CreateMandateCommand command) throws MandateAlreadyCreatedException {

        String id = command.getId();

        if (eventRepository.findAll(id) != null) {
            throw new MandateAlreadyCreatedException(id);
        }

        MandateCreatedEvent event = new MandateCreatedEvent(id, command.getBankName(), command.getContractor());
        if (!command.getOtherHeirs().isEmpty()) {
            event.getOtherHeirs().addAll(command.getOtherHeirs());
        }
        event.setMainHeir(command.getMainHeir());
        event.setNotary(command.getNotary());

        eventRepository.storeAndPublish(event);
    }

    @Override
    public void apply(AddHeirCommand command) throws MandateNotYetCreatedException {
        try {
            String id = command.getId();
            List<IEvent> allEvents = eventRepository.findAll(id);
            // Apply events to create latest state of mandate
            try (MandateProjector mandateProjector = new MandateProjector()) {
                Mandate mandate = mandateProjector.project(allEvents);
                // Generate a MandateHeirAddedEvent only for heirs which are not yet added
                command.getOtherHeirs().stream().
                        filter(heir -> !mandate.getOtherHeirs().contains(heir)).
                        forEach(heir -> {
                            MandateHeirAddedEvent event = new MandateHeirAddedEvent(id, heir);
                            eventRepository.storeAndPublish(event);
                        });
            }
        } catch (MandateNotYetCreatedException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void apply(RemoveHeirCommand command) throws MandateNotYetCreatedException {
        try {
            String id = command.getId();
            List<IEvent> allEvents = eventRepository.findAll(id);
            // Apply events to create latest state of mandate
            try (MandateProjector mandateProjector = new MandateProjector()) {
                Mandate mandate = mandateProjector.project(allEvents);
                // Generate a MandateHeirRemovedEvent only for heirs which are not yet removed
                command.getOtherHeirs().stream().
                        filter(heir -> mandate.getOtherHeirs().contains(heir)).
                        forEach(heir -> {
                            MandateHeirRemovedEvent event = new MandateHeirRemovedEvent(id, heir);
                            eventRepository.storeAndPublish(event);
                        });
            }
        } catch (MandateNotYetCreatedException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void apply(DefineMainHeirCommand command) throws MandateNotYetCreatedException {
        try {
            String id = command.getId();
            List<IEvent> allEvents = eventRepository.findAll(id);
            // Apply events to create latest state of mandate
            try (MandateProjector mandateProjector = new MandateProjector()) {
                Mandate mandate = mandateProjector.project(allEvents);
                // Generate a MandateMainHeirDefinedEvent only if this main heir is not yet defined
                if (!Objects.equals(mandate.getMainHeir(), command.getMainHeir())) {
                    MandateMainHeirDefinedEvent event = new MandateMainHeirDefinedEvent(id, command.getMainHeir());
                    eventRepository.storeAndPublish(event);
                }
            }
        } catch (MandateNotYetCreatedException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void apply(DefineNotaryCommand command) throws MandateNotYetCreatedException {
        try {
            String id = command.getId();
            List<IEvent> allEvents = eventRepository.findAll(id);
            // Apply events to project to  mandate
            try (MandateProjector mandateProjector = new MandateProjector()) {
                Mandate mandate = mandateProjector.project(allEvents);
                // Generate a MandateNotaryDefinedEvent only if this notary is not yet defined
                if (!Objects.equals(mandate.getNotary(), command.getNotary())) {
                    MandateNotaryDefinedEvent event = new MandateNotaryDefinedEvent(id, command.getNotary());
                    eventRepository.storeAndPublish(event);
                }
            }
        } catch (MandateNotYetCreatedException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
