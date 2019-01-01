package com.ote.mandate.business;

import com.ote.framework.IEvent;
import com.ote.mandate.business.spi.IEventRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventRepositoryMock implements IEventRepository {

    private Map<Object, List<IEvent>> eventStore = new HashMap<>();

    @Override
    public void storeAndPublish(IEvent event) {
        System.out.println("Storing and publishing event " + event.getClass().getTypeName());
        eventStore.computeIfAbsent(event.getId(), p -> new ArrayList<>()).add(event);
    }

    @Override
    public List<IEvent> findAll(String id) {
        return eventStore.get(id);
    }

    public void clean() {
        eventStore.clear();
    }
}
