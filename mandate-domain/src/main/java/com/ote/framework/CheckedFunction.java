package com.ote.framework;

@FunctionalInterface
public interface CheckedFunction<T, R> {

    R apply(T param) throws Throwable;
}
