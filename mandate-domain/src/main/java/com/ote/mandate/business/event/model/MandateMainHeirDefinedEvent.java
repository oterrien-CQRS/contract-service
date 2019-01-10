package com.ote.mandate.business.event.model;

import com.ote.mandate.business.aggregate.Heir;
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
public class MandateMainHeirDefinedEvent extends BaseMandateEvent {

    @NotNull
    @Valid
    private Heir heir;

    public MandateMainHeirDefinedEvent(String id, Heir heir) {
        super(id);
        this.heir = heir;
    }
}
