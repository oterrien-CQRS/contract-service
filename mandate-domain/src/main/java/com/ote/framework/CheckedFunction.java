package com.ote.framework;


public interface CheckedFunction {

    @FunctionalInterface
    interface Function1<T1, R> {
        R apply(T1 param1) throws Throwable;
    }

    @FunctionalInterface
    interface Function2<T1, T2, R> {
        R apply(T1 param1, T2 param2) throws Throwable;
    }
}
