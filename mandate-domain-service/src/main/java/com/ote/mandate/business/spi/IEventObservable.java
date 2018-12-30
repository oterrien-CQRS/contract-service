package com.ote.mandate.business.spi;

import com.ote.framework.IEvent;
import com.ote.framework.IEventHandler;

public interface IEventObservable {

    <TE extends IEvent> void subscribe(Class<TE> eventClass, IEventHandler<TE> consumer);
}
