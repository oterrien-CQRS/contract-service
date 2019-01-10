package com.ote.mandate.business.event.api;

import com.ote.mandate.business.event.business.MandateEventHandlerFactory;
import lombok.Getter;

public final class MandateEventHandlerProvider {

    @Getter
    private static final MandateEventHandlerProvider Instance = new MandateEventHandlerProvider();

    @Getter
    private final MandateEventHandlerFactory handlerFactory;

    private MandateEventHandlerProvider() {
        this.handlerFactory = new MandateEventHandlerFactory();
    }
}
