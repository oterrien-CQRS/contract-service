package com.ote.mandate.business.event.business;

import com.ote.mandate.business.event.api.IMandateEventHandler;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public final class MandateEventHandlerFactory {

    public IMandateEventHandler createEventHandler() {
        return new MandateEventHandler();
    }
}
