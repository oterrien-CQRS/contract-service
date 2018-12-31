package com.ote.mandate.business.model.event;

import com.ote.mandate.business.model.aggregate.Notary;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString(callSuper = true)
public class MandateNotaryDefinedEvent extends BaseMandateEvent {

    private final Notary notary;

    public MandateNotaryDefinedEvent(String id, Notary notary) {
        super(id);
        this.notary = notary;
    }
}
