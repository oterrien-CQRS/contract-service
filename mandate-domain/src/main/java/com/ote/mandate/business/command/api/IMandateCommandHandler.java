package com.ote.mandate.business.command.api;

public interface IMandateCommandHandler extends
        ICreateMandateCommandHandler,
        IAddHeirsCommandHandler,
        IRemoveHeirsCommandHandler,
        IDefineMainHeirCommandHandler,
        IDefineNotaryCommandHandler {
}
