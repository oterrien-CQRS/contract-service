package com.ote.framework;

@FunctionalInterface
public interface IEventHandler<TE extends IEvent> {

    void handle(TE event) throws Exception;
}
