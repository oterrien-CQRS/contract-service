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

    default <T extends ICommand> Mono<Tuple2<T, List<IEvent>>> getOrRaiseError(T command,
                                                                               Predicate<List<IEvent>> successCondition,
                                                                               Supplier<Throwable> errorRaised,
                                                                               IEventRepository eventRepository) {
        return eventRepository.
                findAll(Mono.just(command.getId())).
                collectList().
                flatMap(events -> {
                    if (successCondition.test(events)) {
                        return Mono.just(Tuples.of(command, events));
                    } else {
                        return Mono.error(errorRaised.get());
                    }
                });
    }
}
