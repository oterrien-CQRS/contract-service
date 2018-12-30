package com.ote.mandate.business.domain;

import com.ote.mandate.business.api.IMandateService;
import com.ote.mandate.business.spi.IEventObservable;
import com.ote.mandate.business.spi.IEventPublisher;
import com.ote.mandate.business.spi.IMandateRepository;

public class MandateServiceFactory {

    public IMandateService createService(IEventPublisher eventPublisher, IEventObservable eventObservable, IMandateRepository mandateRepository) {
        return new MandateService(eventPublisher, eventObservable, mandateRepository);
    }
}
