package com.ote.mandate.business.domain;

import com.ote.framework.IEvent;
import com.ote.mandate.business.api.IRemoveHeirsCommandService;
import com.ote.mandate.business.exception.MandateNotYetCreatedException;
import com.ote.mandate.business.model.aggregate.Mandate;
import com.ote.mandate.business.model.aggregate.MandateProjector;
import com.ote.mandate.business.model.command.RemoveHeirsCommand;
import com.ote.mandate.business.model.event.MandateHeirRemovedEvent;
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
public class RemoveHeirsCommandService implements IRemoveHeirsCommandService {

    private final IEventRepository eventRepository;

    @Override
    public Mono<Boolean> removeHeirs(Mono<RemoveHeirsCommand> command) {

        return command.
                doOnNext(cmd -> log.debug("Trying to remove heirs : {}", cmd)).
                flatMap(cmd -> getOrRaiseError(cmd, events -> CollectionUtils.isNotEmpty(events), () -> new MandateNotYetCreatedException(cmd.getId()), eventRepository)).
                map(tuple -> {
                    List<IEvent> events = tuple.getT2();
                    RemoveHeirsCommand cmd = tuple.getT1();
                    return createEvents(cmd, events);
                }).
                flatMapMany(events -> Flux.fromStream(events.stream())).
                flatMap(event -> eventRepository.storeAndPublish(Mono.just(event))).
                collectList().
                map(list -> list.stream().anyMatch(p -> p == true));
    }

    private List<IEvent> createEvents(RemoveHeirsCommand command, List<IEvent> events) {

        try (MandateProjector mandateProjector = new MandateProjector()) {
            Mandate mandate = mandateProjector.apply(events);
            return command.getOtherHeirs().stream().
                    filter(heir -> CollectionUtils.containsAny(mandate.getOtherHeirs(), heir)).
                    map(heir -> new MandateHeirRemovedEvent(command.getId(), heir)).
                    collect(Collectors.toList());
        } catch (Exception e) {
            throw Exceptions.propagate(e);
        }
    }
}
