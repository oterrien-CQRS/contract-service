package com.ote.mandate.business.domain;

import com.ote.framework.Validable;
import com.ote.mandate.business.api.IMandateCommandService;
import com.ote.mandate.business.exception.MalformedCommandException;
import com.ote.mandate.business.exception.MandateAlreadyCreatedException;
import com.ote.mandate.business.exception.MandateNotYetCreatedException;
import com.ote.mandate.business.model.command.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Slf4j
public class ValidMandateCommandService implements IMandateCommandService {

    private final IMandateCommandService mandateCommandService;

    @Override
    public Mono<Boolean> createMandate(Mono<CreateMandateCommand> command) throws MalformedCommandException, MandateAlreadyCreatedException {
        try {
            command.validate();
            return mandateCommandService.createMandate(command);
        } catch (Validable.NotValidException e) {
            throw new MalformedCommandException(e);
        } finally {
            log.debug("Command {} is valid", command);
        }
    }

    @Override
    public Mono<Boolean> addHeir(Mono<AddHeirCommand> command) throws MalformedCommandException, MandateNotYetCreatedException {
        try {
            command.validate();
            return mandateCommandService.addHeir(command);
        } catch (Validable.NotValidException e) {
            throw new MalformedCommandException(e);
        } finally {
            log.debug("Command {} is valid", command);
        }
    }

    @Override
    public Mono<Boolean> removeHeir(Mono<RemoveHeirCommand> command) throws MalformedCommandException, MandateNotYetCreatedException {
        try {
            command.validate();
            return mandateCommandService.removeHeir(command);
        } catch (Validable.NotValidException e) {
            throw new MalformedCommandException(e);
        } finally {
            log.debug("Command {} is valid", command);
        }
    }

    @Override
    public Mono<Boolean> defineMainHeir(Mono<DefineMainHeirCommand> command) throws MalformedCommandException, MandateNotYetCreatedException {
        try {
            command.validate();
            return mandateCommandService.defineMainHeir(command);
        } catch (Validable.NotValidException e) {
            throw new MalformedCommandException(e);
        } finally {
            log.debug("Command {} is valid", command);
        }
    }

    @Override
    public Mono<Boolean> defineNotary(Mono<DefineNotaryCommand> command) throws MalformedCommandException, MandateNotYetCreatedException {
        try {
            command.validate();
            return mandateCommandService.defineNotary(command);
        } catch (Validable.NotValidException e) {
            throw new MalformedCommandException(e);
        } finally {
            log.debug("Command {} is valid", command);
        }
    }
}
