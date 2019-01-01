package com.ote.mandate.business.model.event;

import com.ote.mandate.business.model.aggregate.Notary;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Getter
@ToString(callSuper = true)
public class MandateNotaryDefinedEvent extends BaseMandateEvent {

    @NotNull
    private final Notary notary;

    public MandateNotaryDefinedEvent(String id, Notary notary) {
        super(id);
        this.notary = notary;
    }
}
