package com.ote.mandate.business.model.event;

import com.ote.framework.IEvent;
import com.ote.mandate.business.model.Heir;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString(callSuper = true)
public class MandateHeirAddedEvent extends BaseMandateEvent implements IEvent {

    private final Heir heir;

    public MandateHeirAddedEvent(String id, Heir heir) {
        super(id);
        this.heir = heir;
    }
}
