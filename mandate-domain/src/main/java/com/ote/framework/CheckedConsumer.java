package com.ote.framework;


public interface CheckedConsumer {

    @FunctionalInterface
    interface Consumer1<T1> {
        void apply(T1 param1) throws Exception;
    }

    @FunctionalInterface
    interface Consumer2<T1, T2> {
        void apply(T1 param1, T2 param2) throws Exception;
    }
}
