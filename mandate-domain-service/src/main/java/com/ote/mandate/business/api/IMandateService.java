package com.ote.mandate.business.api;

import com.ote.mandate.business.exception.*;
import com.ote.mandate.business.model.command.AddHeirCommand;
import com.ote.mandate.business.model.command.CreateMandateCommand;
import com.ote.mandate.business.model.command.DefineMainHeirCommand;
import com.ote.mandate.business.model.command.DefineNotaryCommand;

public interface IMandateService {

    void apply(CreateMandateCommand command) throws MandateAlreadyCreatedException;

    void apply(AddHeirCommand command) throws MandateNotYetCreatedException, MandateAllHeirAlreadyPresentException;

    void apply(DefineMainHeirCommand command) throws MandateNotYetCreatedException, MandateMainHeirIsAlreadyDefinedException;

    void apply(DefineNotaryCommand command) throws MandateNotYetCreatedException, MandateNotaryIsAlreadyDefinedException;
}
