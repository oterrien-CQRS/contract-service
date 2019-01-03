package com.ote.mandate.business.api;

import com.ote.mandate.business.exception.MalformedCommandException;
import com.ote.mandate.business.exception.MandateAlreadyCreatedException;
import com.ote.mandate.business.exception.MandateNotYetCreatedException;
import com.ote.mandate.business.model.command.*;

public interface IMandateCommandService {

    void apply(CreateMandateCommand command) throws MalformedCommandException, MandateAlreadyCreatedException;

    void apply(AddHeirCommand command) throws MalformedCommandException, MandateNotYetCreatedException;

    void apply(RemoveHeirCommand command) throws MalformedCommandException, MandateNotYetCreatedException;

    void apply(DefineMainHeirCommand command) throws MalformedCommandException, MandateNotYetCreatedException;

    void apply(DefineNotaryCommand command) throws MalformedCommandException, MandateNotYetCreatedException;
}
