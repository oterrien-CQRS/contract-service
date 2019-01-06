package com.ote.mandate.business.api;

import com.ote.framework.ICommand;
import com.ote.framework.IEvent;
import com.ote.mandate.business.spi.IEventRepository;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;

public interface ICommandHandler {

    default <T extends ICommand> Mono<Tuple2<T, List<IEvent>>> getOrRaiseError(T command, Predicate<List<IEvent>> successCondition, Supplier<Exception> errorRaised, IEventRepository eventRepository) {

        return eventRepository.
                findAll(Mono.just(command.getId())).
                collectList().
                filter(successCondition).
                map(events -> Tuples.of(command, events)).
                switchIfEmpty(Mono.error(errorRaised.get()));
    }
}
