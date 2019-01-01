package com.ote.mandate.business.domain;

import com.ote.framework.Validable;
import com.ote.mandate.business.api.IMandateCommandService;
import com.ote.mandate.business.exception.MalformedCommandException;
import com.ote.mandate.business.exception.MandateAlreadyCreatedException;
import com.ote.mandate.business.exception.MandateNotYetCreatedException;
import com.ote.mandate.business.model.command.*;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ValidMandateCommandService implements IMandateCommandService {

    private final IMandateCommandService mandateCommandService;

    @Override
    public void apply(CreateMandateCommand command) throws MalformedCommandException, MandateAlreadyCreatedException {
        try {
            command.validate();
            mandateCommandService.apply(command);
        } catch (Validable.NotValidException e) {
            throw new MalformedCommandException(e);
        }
    }

    @Override
    public void apply(AddHeirCommand command) throws MalformedCommandException, MandateNotYetCreatedException {
        try {
            command.validate();
            mandateCommandService.apply(command);
        } catch (Validable.NotValidException e) {
            throw new MalformedCommandException(e);
        }
    }

    @Override
    public void apply(RemoveHeirCommand command) throws MalformedCommandException, MandateNotYetCreatedException {
        try {
            command.validate();
            mandateCommandService.apply(command);
        } catch (Validable.NotValidException e) {
            throw new MalformedCommandException(e);
        }
    }

    @Override
    public void apply(DefineMainHeirCommand command) throws MalformedCommandException, MandateNotYetCreatedException {
        try {
            command.validate();
            mandateCommandService.apply(command);
        } catch (Validable.NotValidException e) {
            throw new MalformedCommandException(e);
        }
    }

    @Override
    public void apply(DefineNotaryCommand command) throws MalformedCommandException, MandateNotYetCreatedException {
        try {
            command.validate();
            mandateCommandService.apply(command);
        } catch (Validable.NotValidException e) {
            throw new MalformedCommandException(e);
        }
    }
}
