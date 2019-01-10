package com.ote.mandate.business.command.api;

import com.ote.mandate.business.command.exception.MalformedCommandException;
import com.ote.mandate.business.command.exception.MandateNotYetCreatedException;
import com.ote.mandate.business.command.model.RemoveHeirsCommand;
import reactor.core.publisher.Mono;

public interface IRemoveHeirsCommandHandler extends ICommandHandler {

    Mono<Boolean> removeHeirs(Mono<RemoveHeirsCommand> command) throws MalformedCommandException, MandateNotYetCreatedException;
}
