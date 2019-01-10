package com.ote.mandate.business.command.api;

import com.ote.mandate.business.command.exception.MalformedCommandException;
import com.ote.mandate.business.command.exception.MandateNotYetCreatedException;
import com.ote.mandate.business.command.model.DefineNotaryCommand;
import reactor.core.publisher.Mono;

public interface IDefineNotaryCommandHandler extends ICommandHandler {

    Mono<Boolean> defineNotary(Mono<DefineNotaryCommand> command) throws MalformedCommandException, MandateNotYetCreatedException;
}
