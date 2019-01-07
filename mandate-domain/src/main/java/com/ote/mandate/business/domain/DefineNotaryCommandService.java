package com.ote.mandate.business.domain;

import com.ote.framework.IEvent;
import com.ote.mandate.business.api.IDefineNotaryCommandService;
import com.ote.mandate.business.exception.MandateNotYetCreatedException;
import com.ote.mandate.business.model.aggregate.Mandate;
import com.ote.mandate.business.model.aggregate.MandateProjector;
import com.ote.mandate.business.model.command.DefineNotaryCommand;
import com.ote.mandate.business.model.event.MandateNotaryDefinedEvent;
import com.ote.mandate.business.spi.IEventRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import reactor.core.Exceptions;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class DefineNotaryCommandService implements IDefineNotaryCommandService {

    private final IEventRepository eventRepository;

    @Override
    public Mono<Boolean> defineNotary(Mono<DefineNotaryCommand> command) {

        return command.
                doOnNext(cmd -> log.debug("Trying to define notary : {}", cmd)).
                flatMap(cmd -> getOrRaiseError(cmd, events -> CollectionUtils.isNotEmpty(events), () -> new MandateNotYetCreatedException(cmd.getId()), eventRepository)).
                map(tuple -> {
                    DefineNotaryCommand cmd = tuple.getT1();
                    List<IEvent> events = tuple.getT2();
                    return createEvents(cmd, events);
                }).
                flatMap(event -> event.map(p -> eventRepository.storeAndPublish(Mono.just(p))).orElse(Mono.just(false)));
    }

    private Optional<IEvent> createEvents(DefineNotaryCommand command, List<IEvent> events) {

        try (MandateProjector mandateProjector = new MandateProjector()) {
            Mandate mandate = mandateProjector.apply(events);
            if (!Objects.equals(mandate.getNotary(), command.getNotary())) {
                return Optional.of(new MandateNotaryDefinedEvent(command.getId(), command.getNotary()));
            } else {
                return Optional.empty();
            }
        } catch (Exception e) {
            throw Exceptions.propagate(e);
        }
    }
}
