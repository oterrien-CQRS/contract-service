package com.ote.mandate.business.command.domain;

import com.ote.common.cqrs.IEvent;
import com.ote.mandate.business.aggregate.Mandate;
import com.ote.mandate.business.aggregate.MandateProjector;
import com.ote.mandate.business.command.api.IDefineMainHeirCommandHandler;
import com.ote.mandate.business.command.exception.MandateNotYetCreatedException;
import com.ote.mandate.business.command.model.DefineMainHeirCommand;
import com.ote.mandate.business.command.spi.IEventRepository;
import com.ote.mandate.business.event.model.MandateMainHeirDefinedEvent;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import reactor.core.Exceptions;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
class DefineMainHeirCommandHandler implements IDefineMainHeirCommandHandler {

    private final IEventRepository eventRepository;

    @Override
    public Mono<Boolean> defineMainHeir(Mono<DefineMainHeirCommand> command) {

        return command.
                doOnNext(cmd -> log.debug("Trying to define main heir : {}", cmd)).
                flatMap(cmd -> getOrRaiseError(cmd, events -> CollectionUtils.isNotEmpty(events), () -> new MandateNotYetCreatedException(cmd.getId()), eventRepository)).
                map(this::createEvent).
                flatMap(event -> event.map(p -> eventRepository.storeAndPublish(Mono.just(p))).orElse(Mono.just(false)));
    }

    private Optional<IEvent> createEvent(Tuple2<DefineMainHeirCommand, List<IEvent>> tuple) {
        return createEvent(tuple.getT1(), tuple.getT2());
    }

    private Optional<IEvent> createEvent(DefineMainHeirCommand command, List<IEvent> events) {

        try (MandateProjector mandateProjector = new MandateProjector()) {
            Mandate mandate = mandateProjector.apply(events);
            if (!Objects.equals(mandate.getMainHeir(), command.getMainHeir())) {
                return Optional.of(new MandateMainHeirDefinedEvent(command.getId(), command.getMainHeir()));
            } else {
                return Optional.empty();
            }
        } catch (Exception e) {
            throw Exceptions.propagate(e);
        }
    }
}
