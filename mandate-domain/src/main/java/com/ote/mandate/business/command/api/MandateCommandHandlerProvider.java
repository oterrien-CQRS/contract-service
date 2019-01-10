package com.ote.mandate.business.command.api;

import com.ote.mandate.business.command.domain.MandateCommandHandlerFactory;
import lombok.Getter;

public final class MandateCommandHandlerProvider {

    @Getter
    private static final MandateCommandHandlerProvider Instance = new MandateCommandHandlerProvider();

    @Getter
    private final MandateCommandHandlerFactory handlerFactory;

    private MandateCommandHandlerProvider() {
        this.handlerFactory = new MandateCommandHandlerFactory();
    }
}
