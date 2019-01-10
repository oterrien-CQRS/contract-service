package com.ote.mandate.business.command.domain;

import com.ote.mandate.business.command.api.IMandateCommandHandler;
import com.ote.mandate.business.command.spi.IEventRepository;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public final class MandateCommandHandlerFactory {

    public IMandateCommandHandler createService(IEventRepository eventRepository) {
        return new ValidMandateCommandHandler(new MandateCommandHandler(eventRepository));
    }
}
