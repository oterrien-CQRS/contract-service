package com.ote.mandate.business.spi;

import com.ote.framework.IEvent;

public interface IEventPublisher {

    void storeAndPublish(IEvent event);

}

