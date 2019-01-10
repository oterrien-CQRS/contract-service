package com.ote.common;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

public final class Convertor {

    private final Map<String, Function<?, ?>> mapper = new HashMap<>();

    public <T, R> void bind(Class<T> type, Function<T, R> mapper) {
        this.mapper.put(type.getTypeName(), mapper);
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