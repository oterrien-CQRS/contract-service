package com.ote.framework;

import java.util.List;

public interface IProjector<T> {

    T project(List<IEvent> events) throws Exception;
}
