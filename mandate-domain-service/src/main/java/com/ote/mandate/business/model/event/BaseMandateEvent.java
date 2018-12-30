package com.ote.mandate.business.model.event;

import com.ote.framework.IEvent;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RequiredArgsConstructor
@Getter
@ToString
public abstract class BaseMandateEvent implements IEvent {

    private final String id;
}
