package com.ote.mandate.business.model.event;

import com.ote.mandate.business.model.aggregate.Notary;
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
public class MandateNotaryDefinedEvent extends BaseMandateEvent {

    @NotNull
    @Valid
    private Notary notary;

    public MandateNotaryDefinedEvent(String id, Notary notary) {
        super(id);
        this.notary = notary;
    }
}
