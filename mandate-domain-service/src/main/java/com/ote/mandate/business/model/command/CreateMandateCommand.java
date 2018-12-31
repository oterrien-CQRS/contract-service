package com.ote.mandate.business.model.command;

import com.ote.mandate.business.model.aggregate.Contractor;
import com.ote.mandate.business.model.aggregate.Heir;
import com.ote.mandate.business.model.aggregate.Notary;
import lombok.Getter;

import java.util.List;

@Getter
public class CreateMandateCommand extends BaseMandateCommand {

    private final String bankName;
    private final Contractor contractor;
    private final Notary notary;
    private final Heir mainHeir;
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
