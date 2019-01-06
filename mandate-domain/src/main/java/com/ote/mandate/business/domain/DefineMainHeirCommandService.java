package com.ote.mandate.business.domain;

import com.ote.framework.IEvent;
import com.ote.mandate.business.api.IDefineMainHeirCommandService;
import com.ote.mandate.business.exception.MandateNotYetCreatedException;
import com.ote.mandate.business.model.aggregate.Mandate;
import com.ote.mandate.business.model.aggregate.MandateProjector;
import com.ote.mandate.business.model.command.DefineMainHeirCommand;
import com.ote.mandate.business.model.event.MandateMainHeirDefinedEvent;
import com.ote.mandate.business.spi.IEventRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.Exceptions;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class DefineMainHeirCommandService implements IDefineMainHeirCommandService {

    private final IEventRepository eventRepository;

    @Override
    public Mono<Boolean> defineMainHeir(Mono<DefineMainHeirCommand> command) {

        return command.
                doOnNext(cmd -> log.debug("Trying to define main heir : {}", cmd)).
                flatMap(cmd -> getOrRaiseError(cmd, event -> !event.isEmpty(), () -> new MandateNotYetCreatedException(cmd.getId()), eventRepository)).
                map(tuple -> {
                    DefineMainHeirCommand cmd = tuple.getT1();
                    List<IEvent> events = tuple.getT2();
                    return createEvents(cmd, events);
                }).
                filter(opt -> opt.isPresent()).
                map(opt -> opt.get()).
                transform(event -> eventRepository.storeAndPublish(event));
    }

    private Optional<IEvent> createEvents(DefineMainHeirCommand command, List<IEvent> events) {

        try (MandateProjector mandateProjector = new MandateProjector()) {
            Mandate mandate = mandateProjector.project(events);

            if (!Objects.equals(mandate.getMainHeir(), command.getMainHeir())) {
                return Optional.of(new MandateMainHeirDefinedEvent(command.getId(), command.getMainHeir()));
            }
            return Optional.empty();
        } catch (Exception e) {
            throw Exceptions.propagate(e);
        }
    }
}
