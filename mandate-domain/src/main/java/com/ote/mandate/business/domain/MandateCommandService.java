package com.ote.mandate.business.domain;

import com.ote.framework.ICommand;
import com.ote.framework.IEvent;
import com.ote.mandate.business.api.IMandateCommandService;
import com.ote.mandate.business.exception.MandateAlreadyCreatedException;
import com.ote.mandate.business.exception.MandateNotYetCreatedException;
import com.ote.mandate.business.model.aggregate.Mandate;
import com.ote.mandate.business.model.aggregate.MandateProjector;
import com.ote.mandate.business.model.command.*;
import com.ote.mandate.business.model.event.MandateCreatedEvent;
import com.ote.mandate.business.model.event.MandateHeirAddedEvent;
import com.ote.mandate.business.spi.IEventRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import reactor.core.Exceptions;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import java.util.stream.Collectors;

@Slf4j
class MandateCommandService implements IMandateCommandService {

    private final IEventRepository eventRepository;

    MandateCommandService(IEventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Override
    public Mono<Boolean> createMandate(Mono<CreateMandateCommand> command) {

        return command.
                doOnNext(cmd -> log.debug("Trying to create mandate : {} ", cmd)).
                flatMap(this::raiseErrorIfEventsExists).
                map(this::createEvent).
                flatMap(eventRepository::storeAndPublish).
                defaultIfEmpty(false);
    }

    private <T extends ICommand> Mono<T> raiseErrorIfEventsExists(T command) {

        return eventRepository.
                findAll(Mono.just(command.getId())).
                hasElements().
                map(hasElements -> {
                    if (hasElements) {
                        throw Exceptions.propagate(new MandateAlreadyCreatedException(command.getId()));
                    } else {
                        return command;
                    }
                });
    }

    private Mono<IEvent> createEvent(CreateMandateCommand command) {

        MandateCreatedEvent event = new MandateCreatedEvent(command.getId(), command.getBankName(), command.getContractor());
        if (!command.getOtherHeirs().isEmpty()) {
            event.getOtherHeirs().addAll(command.getOtherHeirs());
        }
        event.setMainHeir(command.getMainHeir());
        event.setNotary(command.getNotary());
        return Mono.just(event);
    }

    @Override
    public Mono<Boolean> addHeirs(Mono<AddHeirCommand> command) {

        return command.
                doOnNext(cmd -> log.debug("Trying to add heirs : {}", cmd)).
                flatMap(this::raiseErrorIfNoEventExists).
                transform(this::project).
                flatMapMany(tuple -> Flux.fromStream(tuple.getT1().getOtherHeirs().stream().
                        filter(heir -> !tuple.getT2().getOtherHeirs().contains(heir)).
                        map(heir -> new MandateHeirAddedEvent(tuple.getT1().getId(), heir)))
                ).
                flatMap(evt -> eventRepository.storeAndPublish(Mono.just(evt))).
                collect(Collectors.toList()).
                map(list -> CollectionUtils.isNotEmpty(list) && list.stream().allMatch(p -> p)).
                defaultIfEmpty(false);
    }

    private <T extends ICommand> Mono<Tuple2<T, Flux<IEvent>>> raiseErrorIfNoEventExists(T command) {

        return eventRepository.
                findAll(Mono.just(command.getId())).
                transform(events -> events.
                        hasElements().
                        map(hasElements -> {
                            if (hasElements) {
                                return Tuples.of(command, events);
                            } else {
                                throw Exceptions.propagate(new MandateNotYetCreatedException(command.getId()));
                            }
                        })).
                last();
    }

    private <T extends ICommand> Mono<Tuple2<T, Mandate>> project(Mono<Tuple2<T, Flux<IEvent>>> tuple) {

        return tuple.
                flatMap(t -> t.getT2().
                        transform(events -> {
                            try (MandateProjector mandateProjector = new MandateProjector()) {
                                return mandateProjector.project(events);
                            }
                        }).
                        last().
                        map(p -> Tuples.of(t.getT1(), p)));
    }

    @Override
    public Mono<Boolean> removeHeirs(Mono<RemoveHeirCommand> command) throws MandateNotYetCreatedException {
       /* try {
            log.debug("Trying to remove heirs : {}", command);

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
        }*/
        return Mono.just(true);
    }

    @Override
    public Mono<Boolean> defineMainHeir(Mono<DefineMainHeirCommand> command) throws MandateNotYetCreatedException {
        /*try {
            log.debug("Trying to define main heir : {}", command);

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
        }*/
        return Mono.just(true);
    }

    @Override
    public Mono<Boolean> defineNotary(Mono<DefineNotaryCommand> command) throws MandateNotYetCreatedException {
        /*try {
            log.debug("Trying to define notary : {}", command);

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
        }*/
        return Mono.just(true);
    }
}
