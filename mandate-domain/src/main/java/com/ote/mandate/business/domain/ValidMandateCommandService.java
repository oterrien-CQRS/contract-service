package com.ote.mandate.business.domain;

import com.ote.framework.CheckedFunction;
import com.ote.framework.ICommand;
import com.ote.framework.Validable;
import com.ote.mandate.business.api.IMandateCommandService;
import com.ote.mandate.business.exception.MalformedCommandException;
import com.ote.mandate.business.model.command.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.Exceptions;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Slf4j
public class ValidMandateCommandService implements IMandateCommandService {

    private final IMandateCommandService mandateCommandService;

    @Override
    public Mono<Boolean> createMandate(Mono<CreateMandateCommand> command) {
        return delegateWithValidation(command, mandateCommandService::createMandate);
    }

    @Override
    public Mono<Boolean> addHeirs(Mono<AddHeirCommand> command) {
        return delegateWithValidation(command, mandateCommandService::addHeirs);
    }

    @Override
    public Mono<Boolean> removeHeirs(Mono<RemoveHeirCommand> command) {
        return delegateWithValidation(command, mandateCommandService::removeHeirs);
    }

    @Override
    public Mono<Boolean> defineMainHeir(Mono<DefineMainHeirCommand> command) {
        return delegateWithValidation(command, mandateCommandService::defineMainHeir);
    }

    @Override
    public Mono<Boolean> defineNotary(Mono<DefineNotaryCommand> command) {
        return delegateWithValidation(command, mandateCommandService::defineNotary);
    }

    private static <T extends ICommand> Mono<Boolean> delegateWithValidation(Mono<T> command,
                                                                             CheckedFunction<Mono<T>, Mono<Boolean>> delegateFunction) {
        return command.
                doOnNext(cmd -> log.debug("Trying to validated the command {}", cmd)).
                flatMap(cmd -> {
                    try {
                        cmd.validate();
                        log.debug("Command {} is valid", cmd);
                        return delegateFunction.apply(Mono.just(cmd));
                    } catch (Validable.NotValidException e) {
                        throw Exceptions.propagate(new MalformedCommandException(e));
                    } catch (Throwable e) {
                        throw Exceptions.propagate(e);
                    }
                }).
                defaultIfEmpty(false);
    }
}
