package com.ote.mandate.business.domain;

import com.ote.framework.IEvent;
import com.ote.mandate.business.api.IAddHeirsCommandService;
import com.ote.mandate.business.exception.MandateNotYetCreatedException;
import com.ote.mandate.business.model.aggregate.Mandate;
import com.ote.mandate.business.model.aggregate.MandateProjector;
import com.ote.mandate.business.model.command.AddHeirsCommand;
import com.ote.mandate.business.model.event.MandateHeirAddedEvent;
import com.ote.mandate.business.spi.IEventRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import reactor.core.Exceptions;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class AddHeirsCommandService implements IAddHeirsCommandService {

    private final IEventRepository eventRepository;

    @Override
    public Mono<Boolean> addHeirs(Mono<AddHeirsCommand> command) {

        return command.
                doOnNext(cmd -> log.debug("Trying to add heirs : {}", cmd)).
                flatMap(cmd -> getOrRaiseError(cmd, events -> CollectionUtils.isNotEmpty(events), () -> new MandateNotYetCreatedException(cmd.getId()), eventRepository)).
                map(tuple -> {
                    AddHeirsCommand cmd = tuple.getT1();
                    List<IEvent> events = tuple.getT2();
                    return createEvents(cmd, events);
                }).
                flatMapMany(events -> Flux.fromStream(events.stream())).
                flatMap(event -> eventRepository.storeAndPublish(Mono.just(event))).
                collectList().
                map(list -> list.stream().anyMatch(p -> p == true)).
                defaultIfEmpty(false);
    }

    private List<IEvent> createEvents(AddHeirsCommand command, List<IEvent> events) {

        try (MandateProjector mandateProjector = new MandateProjector()) {
            Mandate mandate = mandateProjector.apply(events);
            return command.getOtherHeirs().stream().
                    filter(heir -> !CollectionUtils.containsAny(mandate.getOtherHeirs(), heir)).
                    map(heir -> new MandateHeirAddedEvent(command.getId(), heir)).
                    collect(Collectors.toList());
        } catch (Exception e) {
            throw Exceptions.propagate(e);
        }
    }
}
