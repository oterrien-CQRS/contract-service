package com.ote.mandate.business.command.api;

import com.ote.mandate.business.command.exception.MalformedCommandException;
import com.ote.mandate.business.command.exception.MandateNotYetCreatedException;
import com.ote.mandate.business.command.model.DefineMainHeirCommand;
import reactor.core.publisher.Mono;

public interface IDefineMainHeirCommandHandler extends ICommandHandler {

    Mono<Boolean> defineMainHeir(Mono<DefineMainHeirCommand> command) throws MalformedCommandException, MandateNotYetCreatedException;
}
