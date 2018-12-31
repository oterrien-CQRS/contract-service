package com.ote.mandate.business.model.command;

import com.ote.mandate.business.model.aggregate.Notary;
import lombok.Getter;

@Getter
public class DefineNotaryCommand extends BaseMandateCommand {

    private final Notary notary;

    public DefineNotaryCommand(String id, Notary notary) {
        super(id);
        this.notary = notary;
    }
}
