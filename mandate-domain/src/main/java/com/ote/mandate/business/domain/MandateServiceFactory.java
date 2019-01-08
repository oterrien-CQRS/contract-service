package com.ote.mandate.business.domain;

import com.ote.mandate.business.api.IMandateCommandService;
import com.ote.mandate.business.spi.IEventRepository;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public final class MandateServiceFactory {

    public IMandateCommandService createService(IEventRepository eventRepository) {
        return new ValidMandateCommandService(new MandateCommandService(eventRepository));
    }
}
