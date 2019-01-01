package com.ote.mandate.business.model.event;

import com.ote.mandate.business.model.aggregate.Heir;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Getter
@ToString(callSuper = true)
public class MandateHeirRemovedEvent extends BaseMandateEvent {

    @NotNull
    private final Heir heir;

    public MandateHeirRemovedEvent(String id, Heir heir) {
        super(id);
        this.heir = heir;
    }
}
