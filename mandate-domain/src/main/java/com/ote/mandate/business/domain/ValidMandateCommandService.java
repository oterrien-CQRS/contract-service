package com.ote.mandate.business.domain;

import com.ote.framework.ICommand;
import com.ote.framework.Validable;
import com.ote.mandate.business.api.IMandateCommandService;
import com.ote.mandate.business.exception.MalformedCommandException;
import com.ote.mandate.business.exception.MandateAlreadyCreatedException;
import com.ote.mandate.business.exception.MandateNotYetCreatedException;
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
    public Mono<Boolean> createMandate(Mono<CreateMandateCommand> command) throws MalformedCommandException, MandateAlreadyCreatedException {

        return command.
                doOnNext(cmd -> log.debug("Trying to validated the command {}", cmd)).
                flatMap(cmd -> {
                    try {
                        validate(cmd);
                        return mandateCommandService.createMandate(command);
                    } catch (Exception e) {
                        throw Exceptions.propagate(e);
                    }
                });
    }

    @Override
    public Mono<Boolean> addHeir(Mono<AddHeirCommand> command) throws MalformedCommandException, MandateNotYetCreatedException {
        /*validate(command);
        return mandateCommandService.addHeir(command);*/
        return Mono.just(true);
    }

    @Override
    public Mono<Boolean> removeHeir(Mono<RemoveHeirCommand> command) throws MalformedCommandException, MandateNotYetCreatedException {
      /*  validate(command);
        return mandateCommandService.removeHeir(command);*/
        return Mono.just(true);
    }

    @Override
    public Mono<Boolean> defineMainHeir(Mono<DefineMainHeirCommand> command) throws MalformedCommandException, MandateNotYetCreatedException {
        /*validate(command);
        return mandateCommandService.defineMainHeir(command);*/
        return Mono.just(true);
    }

    @Override
    public Mono<Boolean> defineNotary(Mono<DefineNotaryCommand> command) throws MalformedCommandException, MandateNotYetCreatedException {
       /* validate(command);
        return mandateCommandService.defineNotary(command);*/
        return Mono.just(true);
    }


    private void validate(ICommand command) throws MalformedCommandException {
        try {
            command.validate();
            log.debug("Command {} is valid", command);
        } catch (Validable.NotValidException e) {
            throw new MalformedCommandException(e);
        }
    }
}
