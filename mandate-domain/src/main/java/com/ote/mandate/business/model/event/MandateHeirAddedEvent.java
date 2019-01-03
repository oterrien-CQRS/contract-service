package com.ote.mandate.business.model.event;

import com.ote.mandate.business.model.aggregate.Heir;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@Getter
@Setter
@ToString(callSuper = true)
public class MandateHeirAddedEvent extends BaseMandateEvent {

    @NotNull
    @Valid
    private Heir heir;

    public MandateHeirAddedEvent(String id, Heir heir) {
        super(id);
        this.heir = heir;
    }
}
