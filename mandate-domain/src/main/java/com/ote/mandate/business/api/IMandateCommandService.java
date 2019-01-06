package com.ote.mandate.business.api;

public interface IMandateCommandService extends
        ICreateMandateCommandService,
        IAddHeirsCommandService,
        IRemoveHeirsCommandService,
        IDefineMainHeirCommandService,
        IDefineNotaryCommandService {
}
