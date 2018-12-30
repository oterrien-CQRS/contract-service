package com.ote.mandate;

import com.ote.framework.IEvent;
import com.ote.framework.IEventHandler;
import com.ote.mandate.business.spi.IEventObservable;
import com.ote.mandate.business.spi.IEventPublisher;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class EventPublisherMock implements IEventPublisher, IEventObservable {

    private Map<String, Set<IEventHandler>> observer = new HashMap<>();

    public <TE extends IEvent> void onEvent(TE event) {
        observer.entrySet().stream().
                filter(p -> event.getClass().getTypeName().equals(p.getKey())).
                findAny().
                map(p -> p.getValue()).
                ifPresent(p -> {
                    p.parallelStream().
                            forEach(
                                    h -> {
                                        try {
                                            System.out.println("Calling " + h.toString());
                                            h.handle(event);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                            );
                });
    }

    @Override
    public <TE extends IEvent> void subscribe(Class<TE> eventClass, IEventHandler<TE> consumer) {
        observer.computeIfAbsent(eventClass.getTypeName(), k -> new HashSet<>()).add(consumer);
    }

    @Override
    public void storeAndPublish(IEvent event) {
        System.out.println("Storing event " + event);
        System.out.println("Publishing event " + event);
        onEvent(event);
    }
}
