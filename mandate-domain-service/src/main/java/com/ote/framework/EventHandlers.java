package com.ote.framework;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public final class EventHandlers {

    private final Map<String, IEventHandler> eventHandlers = new HashMap<>();

    public <T extends IEvent> void bind(Class<T> eventClass, IEventHandler<T> eventHandler) {
        eventHandlers.put(eventClass.getTypeName(), eventHandler);
    }

    @SuppressWarnings("unchecked")
    public void handle(List<IEvent> events) throws Exception {
        try {
            events.forEach(event -> {
                IEventHandler eventHandler = Optional.ofNullable(eventHandlers.get(event.getClass().getTypeName())).
                        orElseThrow(() -> new MissingEventBindingException(event.getClass()));
                try {
                    eventHandler.handle(event);
                } catch (Exception e) {
                    throw new LambdaException(e);
                }
            });
        } catch (LambdaException e) {
            throw (Exception) e.getCause();
        }
    }

    private static class MissingEventBindingException extends RuntimeException {

        private final static String MESSAGE_TEMPLATE = "No binding has been found for event %s -> unable to handle event";

        MissingEventBindingException(Class eventClass) {
            super(String.format(MESSAGE_TEMPLATE, eventClass.getTypeName()));
        }
    }
}