package com.ote.mandate.business.api;

import com.ote.mandate.business.exception.MalformedCommandException;
import com.ote.mandate.business.exception.MandateNotYetCreatedException;
import com.ote.mandate.business.model.command.DefineMainHeirCommand;
import reactor.core.publisher.Mono;

public interface IDefineMainHeirCommandService extends ICommandHandler {

    Mono<Boolean> defineMainHeir(Mono<DefineMainHeirCommand> command) throws MalformedCommandException, MandateNotYetCreatedException;
}
