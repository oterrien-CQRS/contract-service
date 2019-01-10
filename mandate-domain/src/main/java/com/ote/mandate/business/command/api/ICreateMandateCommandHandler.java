package com.ote.mandate.business.command.api;

import com.ote.mandate.business.command.exception.MalformedCommandException;
import com.ote.mandate.business.command.exception.MandateAlreadyCreatedException;
import com.ote.mandate.business.command.model.CreateMandateCommand;
import reactor.core.publisher.Mono;

public interface ICreateMandateCommandHandler extends ICommandHandler {

    Mono<Boolean> createMandate(Mono<CreateMandateCommand> command) throws MalformedCommandException, MandateAlreadyCreatedException;
}
