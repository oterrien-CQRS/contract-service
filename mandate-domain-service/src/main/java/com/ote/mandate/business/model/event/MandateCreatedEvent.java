package com.ote.mandate.business.model.event;

import com.ote.mandate.business.model.aggregate.Contractor;
import com.ote.mandate.business.model.aggregate.Heir;
import com.ote.mandate.business.model.aggregate.Notary;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@ToString(callSuper = true)
public class MandateCreatedEvent extends BaseMandateEvent {

    private final String bankName;

    private final Contractor contractor;

    @Setter
    private Notary notary;

    @Setter
    private Heir mainHeir;

    private final List<Heir> otherHeirs = new ArrayList<>();

    public MandateCreatedEvent(String id, String bankName, Contractor contractor) {
        super(id);
        this.bankName = bankName;
        this.contractor = contractor;
    }
}
