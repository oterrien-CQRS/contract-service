package com.ote.mandate.business.api;

import com.ote.mandate.business.exception.MalformedCommandException;
import com.ote.mandate.business.exception.MandateNotYetCreatedException;
import com.ote.mandate.business.model.command.AddHeirsCommand;
import reactor.core.publisher.Mono;

public interface IAddHeirsCommandService extends ICommandHandler {

    Mono<Boolean> addHeirs(Mono<AddHeirsCommand> command) throws MalformedCommandException, MandateNotYetCreatedException;
}
