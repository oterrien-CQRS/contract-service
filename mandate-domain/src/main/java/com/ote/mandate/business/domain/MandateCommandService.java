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
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
class MandateCommandService implements IMandateCommandService {

    private final IEventRepository eventRepository;

    MandateCommandService(IEventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Override
    public Mono<Boolean> createMandate(Mono<CreateMandateCommand> command) throws MandateAlreadyCreatedException {

        log.debug("Trying to addHeir command : " + command);

        String id = command.getId();

        if (CollectionUtils.isNotEmpty(eventRepository.findAll(id))) {
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
    public Mono<Boolean> addHeir(Mono<AddHeirCommand> command) throws MandateNotYetCreatedException {
        try {
            log.debug("Trying to addHeir command : " + command);

            String id = command.getId();

            List<IEvent> allEvents = eventRepository.findAll(id);
            if (CollectionUtils.isEmpty(allEvents)) {
                throw new MandateNotYetCreatedException(id);
            }

            // Apply events to create latest state of mandate
            try (MandateProjector mandateProjector = new MandateProjector()) {
                Mandate mandate = mandateProjector.project(allEvents);
                // Generate a MandateHeirAddedEvent only for heirs which are not yet added
                AtomicBoolean areElementsProcessed = new AtomicBoolean(false);
                command.getOtherHeirs().stream().
                        filter(heir -> !mandate.getOtherHeirs().contains(heir)).
                        peek(heir -> areElementsProcessed.set(true)).
                        forEach(heir -> {
                            MandateHeirAddedEvent event = new MandateHeirAddedEvent(id, heir);
                            eventRepository.storeAndPublish(event);
                        });

                if (!areElementsProcessed.get()) {
                    log.debug("All these heirs have already been added to mandate {}", id);
                }
            }
        } catch (MandateNotYetCreatedException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Mono<Boolean> removeHeir(Mono<RemoveHeirCommand> command) throws MandateNotYetCreatedException {
        try {
            log.debug("Trying to addHeir command : " + command);

            String id = command.getId();

            List<IEvent> allEvents = eventRepository.findAll(id);
            if (CollectionUtils.isEmpty(allEvents)) {
                throw new MandateNotYetCreatedException(id);
            }

            // Apply events to create latest state of mandate
            try (MandateProjector mandateProjector = new MandateProjector()) {
                Mandate mandate = mandateProjector.project(allEvents);
                // Generate a MandateHeirRemovedEvent only for heirs which are not yet removed
                AtomicBoolean areElementsProcessed = new AtomicBoolean(false);
                command.getOtherHeirs().stream().
                        filter(heir -> mandate.getOtherHeirs().contains(heir)).
                        peek(heir -> areElementsProcessed.set(true)).
                        forEach(heir -> {
                            MandateHeirRemovedEvent event = new MandateHeirRemovedEvent(id, heir);
                            eventRepository.storeAndPublish(event);
                        });

                if (!areElementsProcessed.get()) {
                    log.debug("All these heirs have already been added to mandate {}", id);
                }
            }
        } catch (MandateNotYetCreatedException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Mono<Boolean> defineMainHeir(Mono<DefineMainHeirCommand> command) throws MandateNotYetCreatedException {
        try {
            log.debug("Trying to addHeir command : " + command);

            String id = command.getId();

            List<IEvent> allEvents = eventRepository.findAll(id);
            if (CollectionUtils.isEmpty(allEvents)) {
                throw new MandateNotYetCreatedException(id);
            }

            // Apply events to create latest state of mandate
            try (MandateProjector mandateProjector = new MandateProjector()) {
                Mandate mandate = mandateProjector.project(allEvents);
                // Generate a MandateMainHeirDefinedEvent only if this main heir is not yet defined
                if (!Objects.equals(mandate.getMainHeir(), command.getMainHeir())) {
                    MandateMainHeirDefinedEvent event = new MandateMainHeirDefinedEvent(id, command.getMainHeir());
                    eventRepository.storeAndPublish(event);
                } else {
                    log.debug("This heir is already defined as main for mandate {}", id);
                }
            }
        } catch (MandateNotYetCreatedException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Mono<Boolean> defineNotary(Mono<DefineNotaryCommand> command) throws MandateNotYetCreatedException {
        try {
            log.debug("Trying to addHeir command : " + command);

            String id = command.getId();

            List<IEvent> allEvents = eventRepository.findAll(id);
            if (CollectionUtils.isEmpty(allEvents)) {
                throw new MandateNotYetCreatedException(id);
            }

            // Apply events to project to  mandate
            try (MandateProjector mandateProjector = new MandateProjector()) {
                Mandate mandate = mandateProjector.project(allEvents);
                // Generate a MandateNotaryDefinedEvent only if this notary is not yet defined
                if (!Objects.equals(mandate.getNotary(), command.getNotary())) {
                    MandateNotaryDefinedEvent event = new MandateNotaryDefinedEvent(id, command.getNotary());
                    eventRepository.storeAndPublish(event);
                } else {
                    log.debug("This notary is already defined as main for mandate {}", id);
                }
            }
        } catch (MandateNotYetCreatedException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
