package com.ote.mandate.business.api;

import com.ote.mandate.business.exception.MalformedCommandException;
import com.ote.mandate.business.exception.MandateNotYetCreatedException;
import com.ote.mandate.business.model.command.RemoveHeirsCommand;
import reactor.core.publisher.Mono;

public interface IRemoveHeirsCommandService extends ICommandHandler {

    Mono<Boolean> removeHeirs(Mono<RemoveHeirsCommand> command) throws MalformedCommandException, MandateNotYetCreatedException;
}
