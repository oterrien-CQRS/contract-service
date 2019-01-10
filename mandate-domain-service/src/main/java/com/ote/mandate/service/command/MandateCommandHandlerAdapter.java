package com.ote.mandate.service.command;

import com.ote.mandate.business.command.api.IMandateCommandHandler;
import com.ote.mandate.business.command.api.MandateCommandHandlerProvider;
import com.ote.mandate.business.command.exception.MalformedCommandException;
import com.ote.mandate.business.command.exception.MandateAlreadyCreatedException;
import com.ote.mandate.business.command.exception.MandateNotYetCreatedException;
import com.ote.mandate.business.command.model.*;
import com.ote.mandate.business.command.spi.IEventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class MandateCommandHandlerAdapter implements IMandateCommandHandler {

    private final IMandateCommandHandler mandateCommandService;

    @Autowired
    public MandateCommandHandlerAdapter(IEventRepository eventRepository) {
        this.mandateCommandService = MandateCommandHandlerProvider.getInstance().getHandlerFactory().createService(eventRepository);
    }

    @Override
    public Mono<Boolean> createMandate(Mono<CreateMandateCommand> command) throws MalformedCommandException, MandateAlreadyCreatedException {
        return this.mandateCommandService.createMandate(command);
    }

    @Override
    public Mono<Boolean> addHeirs(Mono<AddHeirsCommand> command) throws MalformedCommandException, MandateNotYetCreatedException {
        return this.mandateCommandService.addHeirs(command);
    }

    @Override
    public Mono<Boolean> removeHeirs(Mono<RemoveHeirsCommand> command) throws MalformedCommandException, MandateNotYetCreatedException {
        return this.mandateCommandService.removeHeirs(command);
    }

    @Override
    public Mono<Boolean> defineMainHeir(Mono<DefineMainHeirCommand> command) throws MalformedCommandException, MandateNotYetCreatedException {
        return this.mandateCommandService.defineMainHeir(command);
    }

    @Override
    public Mono<Boolean> defineNotary(Mono<DefineNotaryCommand> command) throws MalformedCommandException, MandateNotYetCreatedException {
        return this.mandateCommandService.defineNotary(command);
    }
}

