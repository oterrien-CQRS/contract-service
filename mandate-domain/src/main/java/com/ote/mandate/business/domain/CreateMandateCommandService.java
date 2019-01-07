package com.ote.mandate.business.domain;

import com.ote.framework.IEvent;
import com.ote.mandate.business.api.ICreateMandateCommandService;
import com.ote.mandate.business.exception.MandateAlreadyCreatedException;
import com.ote.mandate.business.model.command.CreateMandateCommand;
import com.ote.mandate.business.model.event.MandateCreatedEvent;
import com.ote.mandate.business.spi.IEventRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class CreateMandateCommandService implements ICreateMandateCommandService {

    private final IEventRepository eventRepository;

    @Override
    public Mono<Boolean> createMandate(Mono<CreateMandateCommand> command) {
        return command.
                doOnNext(cmd -> log.debug("Trying to create mandate : {} ", cmd)).
                flatMap(cmd -> getOrRaiseError(cmd, events -> CollectionUtils.isEmpty(events), () -> new MandateAlreadyCreatedException(cmd.getId()), eventRepository)).
                map(tuple -> createEvent(tuple.getT1())).
                flatMap(event -> event.map(p -> eventRepository.storeAndPublish(Mono.just(p))).orElse(Mono.just(false)));
    }

    private Optional<IEvent> createEvent(CreateMandateCommand command) {

        MandateCreatedEvent event = new MandateCreatedEvent(command.getId(), command.getBankName(), command.getContractor());
        if (!command.getOtherHeirs().isEmpty()) {
            event.getOtherHeirs().addAll(command.getOtherHeirs());
        }
        event.setMainHeir(command.getMainHeir());
        event.setNotary(command.getNotary());
        return Optional.of(event);
    }
}
