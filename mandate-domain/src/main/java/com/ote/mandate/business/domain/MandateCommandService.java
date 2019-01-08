package com.ote.mandate.business.domain;

import com.ote.mandate.business.api.*;
import com.ote.mandate.business.exception.MalformedCommandException;
import com.ote.mandate.business.exception.MandateAlreadyCreatedException;
import com.ote.mandate.business.exception.MandateNotYetCreatedException;
import com.ote.mandate.business.model.command.*;
import com.ote.mandate.business.spi.IEventRepository;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
class MandateCommandService implements IMandateCommandService {

    private final ICreateMandateCommandService createMandateCommandService;
    private final IAddHeirsCommandService addHeirsCommandService;
    private final IRemoveHeirsCommandService removeHeirsCommandService;
    private final IDefineMainHeirCommandService defineMainHeirCommandService;
    private final IDefineNotaryCommandService defineNotaryCommandService;

    MandateCommandService(IEventRepository eventRepository) {
        this.createMandateCommandService = new CreateMandateCommandService(eventRepository);
        this.addHeirsCommandService = new AddHeirsCommandService(eventRepository);
        this.removeHeirsCommandService = new RemoveHeirsCommandService(eventRepository);
        this.defineMainHeirCommandService = new DefineMainHeirCommandService(eventRepository);
        this.defineNotaryCommandService = new DefineNotaryCommandService(eventRepository);
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
