package com.ote.framework;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

public final class Convertor {

    private final Map<String, Function<?, ?>> mapper = new HashMap<>();

    public <T, R> void bind(Class<T> eventClass, Function<T, R> eventMapper) {
        this.mapper.put(eventClass.getTypeName(), eventMapper);
    }

    @SuppressWarnings("unchecked")
    public <T, R> R convert(T event) {
        Function function = Optional.
                ofNullable(this.mapper.get(event.getClass().getTypeName())).
                orElseThrow(() -> new MissingBindingException(event.getClass()));
        return (R) function.apply(event);
    }

    private static class MissingBindingException extends RuntimeException {

        private final static String MESSAGE_TEMPLATE = "No binding has been found for %s";

        MissingBindingException(Class eventClass) {
            super(String.format(MESSAGE_TEMPLATE, eventClass.getTypeName()));
        }
    }
}