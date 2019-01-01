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

@Service
public class MandateCommandServiceAdapter implements IMandateCommandService {

    private final IMandateCommandService mandateCommandService;

    @Autowired
    public MandateCommandServiceAdapter(IEventRepository eventRepository) {
        this.mandateCommandService = MandateServiceProvider.getInstance().getFactory().createService(eventRepository);
    }

    @Override
    public void apply(CreateMandateCommand command) throws MalformedCommandException, MandateAlreadyCreatedException {
        this.mandateCommandService.apply(command);
    }

    @Override
    public void apply(AddHeirCommand command) throws MalformedCommandException, MandateNotYetCreatedException {
        this.mandateCommandService.apply(command);
    }

    @Override
    public void apply(RemoveHeirCommand command) throws MalformedCommandException, MandateNotYetCreatedException {
        this.mandateCommandService.apply(command);
    }

    @Override
    public void apply(DefineMainHeirCommand command) throws MalformedCommandException, MandateNotYetCreatedException {
        this.mandateCommandService.apply(command);
    }

    @Override
    public void apply(DefineNotaryCommand command) throws MalformedCommandException, MandateNotYetCreatedException {
        this.mandateCommandService.apply(command);
    }
}

