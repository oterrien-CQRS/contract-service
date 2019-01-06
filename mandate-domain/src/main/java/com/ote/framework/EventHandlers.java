package com.ote.framework;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

public final class EventHandlers implements AutoCloseable {

    private final Map<String, CheckedConsumer<? extends IEvent>> eventHandlers = new HashMap<>();

    public <T extends IEvent> void bind(Class<T> eventClass, CheckedConsumer<T> eventHandler) {
        eventHandlers.put(eventClass.getTypeName(), eventHandler);
    }

    public <T extends IEvent> void handle(T... events) throws Exception {
        try {
            Stream.of(events).forEach(event -> {
                try {
                    handle(event);
                } catch (Exception e) {
                    throw new LambdaException(e);
                }
            });
        } catch (LambdaException e) {
            throw (Exception) e.getCause();
        }
    }

    @SuppressWarnings("unchecked")
    public <T extends IEvent> void handle(T event) throws Exception {
        CheckedConsumer eventHandler = Optional.ofNullable(eventHandlers.get(event.getClass().getTypeName())).
                orElseThrow(() -> new MissingEventBindingException(event.getClass()));
        eventHandler.apply(event);
    }

    @Override
    public void close() {
        eventHandlers.clear();
    }

    private static class MissingEventBindingException extends RuntimeException {

        private final static String MESSAGE_TEMPLATE = "No binding has been found for event %s -> unable to handle event";

        MissingEventBindingException(Class eventClass) {
            super(String.format(MESSAGE_TEMPLATE, eventClass.getTypeName()));
        }
    }
}