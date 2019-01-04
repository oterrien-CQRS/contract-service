package com.ote.mandate.business.api;

import com.ote.mandate.business.exception.MalformedCommandException;
import com.ote.mandate.business.exception.MandateAlreadyCreatedException;
import com.ote.mandate.business.exception.MandateNotYetCreatedException;
import com.ote.mandate.business.model.command.*;
import reactor.core.publisher.Mono;

public interface IMandateCommandService {

    Mono<Boolean> createMandate(CreateMandateCommand command) throws MalformedCommandException, MandateAlreadyCreatedException;

    Mono<Boolean> addHeir(AddHeirCommand command) throws MalformedCommandException, MandateNotYetCreatedException;

    Mono<Boolean> removeHeir(RemoveHeirCommand command) throws MalformedCommandException, MandateNotYetCreatedException;

    Mono<Boolean> defineMainHeir(DefineMainHeirCommand command) throws MalformedCommandException, MandateNotYetCreatedException;

    Mono<Boolean> defineNotary(DefineNotaryCommand command) throws MalformedCommandException, MandateNotYetCreatedException;
}
