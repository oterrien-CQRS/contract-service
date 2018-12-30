package com.ote.framework;

public interface IEventRepository {

    void storeAndPublish(IEvent event);

    <TE extends IEvent> void subscribe(Class<TE> eventClass, IEventHandler<TE> consumer);
}

