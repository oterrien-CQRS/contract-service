package com.ote.mandate.business.api;

import com.ote.mandate.business.exception.MalformedCommandException;
import com.ote.mandate.business.exception.MandateAlreadyCreatedException;
import com.ote.mandate.business.model.command.CreateMandateCommand;
import reactor.core.publisher.Mono;

public interface ICreateMandateCommandService extends ICommandHandler {

    Mono<Boolean> createMandate(Mono<CreateMandateCommand> command) throws MalformedCommandException, MandateAlreadyCreatedException;
}
