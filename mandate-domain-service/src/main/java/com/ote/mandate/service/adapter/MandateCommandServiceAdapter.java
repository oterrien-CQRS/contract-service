package com.ote.mandate.service.adapter;

import com.ote.mandate.business.api.IMandateCommandService;
import com.ote.mandate.business.api.MandateServiceProvider;
import com.ote.mandate.business.exception.MalformedCommandException;
import com.ote.mandate.business.exception.MandateAlreadyCreatedException;
import com.ote.mandate.business.exception.MandateNotYetCreatedException;
import com.ote.mandate.business.model.command.*;
import com.ote.mandate.business.spi.IEventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class MandateCommandServiceAdapter implements IMandateCommandService {

    private final IMandateCommandService mandateCommandService;

    @Autowired
    public MandateCommandServiceAdapter(IEventRepository eventRepository) {
        this.mandateCommandService = MandateServiceProvider.getInstance().getFactory().createService(eventRepository);
    }

    @Override
    public Mono<Boolean> createMandate(Mono<CreateMandateCommand> command) throws MalformedCommandException, MandateAlreadyCreatedException {
        return this.mandateCommandService.createMandate(command);
    }

    @Override
    public Mono<Boolean> addHeir(Mono<AddHeirCommand> command) throws MalformedCommandException, MandateNotYetCreatedException {
        return this.mandateCommandService.addHeir(command);
    }

    @Override
    public Mono<Boolean> removeHeir(Mono<RemoveHeirCommand> command) throws MalformedCommandException, MandateNotYetCreatedException {
        return this.mandateCommandService.removeHeir(command);
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

