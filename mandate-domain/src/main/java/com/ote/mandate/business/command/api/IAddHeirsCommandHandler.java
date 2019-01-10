package com.ote.mandate.business.command.api;

import com.ote.mandate.business.command.exception.MalformedCommandException;
import com.ote.mandate.business.command.exception.MandateNotYetCreatedException;
import com.ote.mandate.business.command.model.AddHeirsCommand;
import reactor.core.publisher.Mono;

public interface IAddHeirsCommandHandler extends ICommandHandler {

    Mono<Boolean> addHeirs(Mono<AddHeirsCommand> command) throws MalformedCommandException, MandateNotYetCreatedException;
}
