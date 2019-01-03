package com.ote.mandate.business.model.event;

import com.ote.mandate.business.model.aggregate.Contractor;
import com.ote.mandate.business.model.aggregate.Heir;
import com.ote.mandate.business.model.aggregate.Notary;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@ToString(callSuper = true)
public class MandateCreatedEvent extends BaseMandateEvent {

    @NotBlank
    private String bankName;

    @NotNull
    private Contractor contractor;

    @Valid
    private Notary notary;

    @Valid
    private Heir mainHeir;

    @Valid
    private final List<Heir> otherHeirs = new ArrayList<>();

    public MandateCreatedEvent(String id, String bankName, Contractor contractor) {
        super(id);
        this.bankName = bankName;
        this.contractor = contractor;
    }
}
