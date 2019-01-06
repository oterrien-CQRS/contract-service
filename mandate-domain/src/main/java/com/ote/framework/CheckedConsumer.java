package com.ote.framework;

@FunctionalInterface
public interface CheckedConsumer<T> {

    void apply(T param) throws Exception;
}
