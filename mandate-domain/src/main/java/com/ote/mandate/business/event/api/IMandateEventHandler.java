package com.ote.mandate.business.event.api;

public interface IMandateEventHandler extends
        IMandateCreatedEventHandler,
        IMandateHeirAddedEventHandler,
        IMandateHeirRemovedEventHandler,
        IMandateMainHeirDefinedEventHandler,
        IMandateNotaryDefinedEventHandler {

}
