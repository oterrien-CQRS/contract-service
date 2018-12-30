package com.ote.mandate.business.domain;

import com.ote.mandate.business.api.IMandateService;
import com.ote.mandate.business.spi.IEventRepository;
import com.ote.mandate.business.spi.IMandateRepository;

public class MandateServiceFactory {

    public IMandateService createService(IEventRepository eventRepository, IMandateRepository mandateRepository) {
        return new MandateService(eventRepository, mandateRepository);
    }
}
