package com.ote.mandate.business.domain;

import com.ote.mandate.business.api.IMandateCommandService;
import com.ote.mandate.business.spi.IEventRepository;

public class MandateServiceFactory {

    public IMandateCommandService createService(IEventRepository eventRepository) {
        return new MandateCommandService(eventRepository);
    }
}
