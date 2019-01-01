package com.ote.mandate.business.model.command;

import com.ote.mandate.business.model.aggregate.Contractor;
import com.ote.mandate.business.model.aggregate.Heir;
import com.ote.mandate.business.model.aggregate.Notary;
import lombok.Getter;
import lombok.ToString;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@ToString(callSuper = true)
public class CreateMandateCommand extends BaseMandateCommand {

    @NotBlank
    @Valid
    private final String bankName;

    @NotNull
    @Valid
    private final Contractor contractor;

    @Valid
    private final Notary notary;

    @Valid
    private final Heir mainHeir;

    @Valid
    private final List<Heir> otherHeirs;

    public CreateMandateCommand(String id, String bankName, Contractor contractor, Notary notary, Heir mainHeir, List<Heir> otherHeirs) {
        super(id);
        this.bankName = bankName;
        this.contractor = contractor;
        this.notary = notary;
        this.mainHeir = mainHeir;
        this.otherHeirs = otherHeirs;
    }
}
