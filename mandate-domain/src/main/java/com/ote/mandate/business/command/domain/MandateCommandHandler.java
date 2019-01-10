package com.ote.mandate.business.command.domain;

import com.ote.mandate.business.command.api.*;
import com.ote.mandate.business.command.exception.MalformedCommandException;
import com.ote.mandate.business.command.exception.MandateAlreadyCreatedException;
import com.ote.mandate.business.command.exception.MandateNotYetCreatedException;
import com.ote.mandate.business.command.model.*;
import com.ote.mandate.business.command.spi.IEventRepository;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
class MandateCommandHandler implements IMandateCommandHandler {

    private final ICreateMandateCommandHandler createMandateCommandService;
    private final IAddHeirsCommandHandler addHeirsCommandService;
    private final IRemoveHeirsCommandHandler removeHeirsCommandService;
    private final IDefineMainHeirCommandHandler defineMainHeirCommandService;
    private final IDefineNotaryCommandHandler defineNotaryCommandService;

    MandateCommandHandler(IEventRepository eventRepository) {
        this.createMandateCommandService = new CreateMandateCommandHandler(eventRepository);
        this.addHeirsCommandService = new AddHeirsCommandHandler(eventRepository);
        this.removeHeirsCommandService = new RemoveHeirsCommandHandler(eventRepository);
        this.defineMainHeirCommandService = new DefineMainHeirCommandHandler(eventRepository);
        this.defineNotaryCommandService = new DefineNotaryCommandHandler(eventRepository);
    }

    @Override
    public Mono<Boolean> createMandate(Mono<CreateMandateCommand> command) throws MalformedCommandException, MandateAlreadyCreatedException {
        return createMandateCommandService.createMandate(command);
    }

    @Override
    public Mono<Boolean> addHeirs(Mono<AddHeirsCommand> command) throws MalformedCommandException, MandateNotYetCreatedException {
        return addHeirsCommandService.addHeirs(command);
    }

    @Override
    public Mono<Boolean> removeHeirs(Mono<RemoveHeirsCommand> command) throws MalformedCommandException, MandateNotYetCreatedException {
        return removeHeirsCommandService.removeHeirs(command);
    }

    @Override
    public Mono<Boolean> defineMainHeir(Mono<DefineMainHeirCommand> command) throws MalformedCommandException, MandateNotYetCreatedException {
        return defineMainHeirCommandService.defineMainHeir(command);
    }

    @Override
    public Mono<Boolean> defineNotary(Mono<DefineNotaryCommand> command) throws MalformedCommandException, MandateNotYetCreatedException {
        return defineNotaryCommandService.defineNotary(command);
    }
}
