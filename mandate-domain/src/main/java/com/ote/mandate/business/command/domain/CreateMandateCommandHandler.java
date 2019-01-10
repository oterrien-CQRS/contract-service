package com.ote.mandate.business.command.domain;

import com.ote.common.cqrs.IEvent;
import com.ote.mandate.business.command.api.ICreateMandateCommandHandler;
import com.ote.mandate.business.command.exception.MandateAlreadyCreatedException;
import com.ote.mandate.business.command.model.CreateMandateCommand;
import com.ote.mandate.business.command.spi.IEventRepository;
import com.ote.mandate.business.event.model.MandateCreatedEvent;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
class CreateMandateCommandHandler implements ICreateMandateCommandHandler {

    private final IEventRepository eventRepository;

    @Override
    public Mono<Boolean> createMandate(Mono<CreateMandateCommand> command) {
        return command.
                doOnNext(cmd -> log.debug("Trying to create mandate : {} ", cmd)).
                flatMap(cmd -> getOrRaiseError(cmd, events -> CollectionUtils.isEmpty(events), () -> new MandateAlreadyCreatedException(cmd.getId()), eventRepository)).
                map(this::createEvent).
                flatMap(event -> event.map(p -> eventRepository.storeAndPublish(Mono.just(p))).orElse(Mono.just(false)));
    }

    private Optional<IEvent> createEvent(Tuple2<CreateMandateCommand, List<IEvent>> tuple) {
        CreateMandateCommand command = tuple.getT1();
        MandateCreatedEvent event = new MandateCreatedEvent(command.getId(), command.getBankName(), command.getContractor());
        if (!command.getOtherHeirs().isEmpty()) {
            event.getOtherHeirs().addAll(command.getOtherHeirs());
        }
        event.setMainHeir(command.getMainHeir());
        event.setNotary(command.getNotary());
        return Optional.of(event);
    }
}
