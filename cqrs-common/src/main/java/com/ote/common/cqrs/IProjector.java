package com.ote.common.cqrs;

import java.util.List;

public interface IProjector<T> extends AutoCloseable {

    T apply(List<IEvent> events) throws Exception;
}
