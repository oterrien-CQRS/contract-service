package com.ote.mandate.business.api;

import com.ote.mandate.business.domain.MandateServiceFactory;
import lombok.Getter;

public final class MandateServiceProvider {

    @Getter
    private static final MandateServiceProvider Instance = new MandateServiceProvider();

    @Getter
    private final MandateServiceFactory factory;

    private MandateServiceProvider() {
        this.factory = new MandateServiceFactory();
    }
}
