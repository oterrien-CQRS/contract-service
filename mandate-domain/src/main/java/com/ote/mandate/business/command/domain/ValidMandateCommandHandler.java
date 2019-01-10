package com.ote.mandate.business.command.domain;

import com.ote.common.CheckedFunction;
import com.ote.common.Validable;
import com.ote.common.cqrs.ICommand;
import com.ote.mandate.business.command.api.IMandateCommandHandler;
import com.ote.mandate.business.command.exception.MalformedCommandException;
import com.ote.mandate.business.command.model.*;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
@Slf4j
class ValidMandateCommandHandler implements IMandateCommandHandler {

    private final IMandateCommandHandler mandateCommandService;

    @Override
    public Mono<Boolean> createMandate(Mono<CreateMandateCommand> command) {
        return withValidation(command, mandateCommandService::createMandate);
    }

    @Override
    public Mono<Boolean> addHeirs(Mono<AddHeirsCommand> command) {
        return withValidation(command, mandateCommandService::addHeirs);
    }

    @Override
    public Mono<Boolean> removeHeirs(Mono<RemoveHeirsCommand> command) {
        return withValidation(command, mandateCommandService::removeHeirs);
    }

    @Override
    public Mono<Boolean> defineMainHeir(Mono<DefineMainHeirCommand> command) {
        return withValidation(command, mandateCommandService::defineMainHeir);
    }

    @Override
    public Mono<Boolean> defineNotary(Mono<DefineNotaryCommand> command) {
        return withValidation(command, mandateCommandService::defineNotary);
    }

    private static <T extends ICommand> Mono<Boolean> withValidation(Mono<T> command,
                                                                     CheckedFunction.Function1<Mono<T>, Mono<Boolean>> delegateFunction) {
        return command.
                doOnNext(cmd -> log.debug("Validating the command {}", cmd)).
                flatMap(cmd -> {
                    try {
                        cmd.validate();
                        log.debug("Command {} is valid", cmd);
                        return delegateFunction.apply(Mono.just(cmd));
                    } catch (Validable.NotValidException e) {
                        return Mono.error(new MalformedCommandException(e));
                    } catch (Throwable e) {
                        return Mono.error(e);
                    }
                });
    }
}
