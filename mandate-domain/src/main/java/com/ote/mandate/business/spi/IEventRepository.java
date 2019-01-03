package com.ote.mandate.business.spi;

import com.ote.framework.IEvent;

import java.util.List;

public interface IEventRepository {

    void storeAndPublish(IEvent event);

    List<IEvent> findAll(String id);
}

