package com.ote.mandate.business.model.command;

import com.ote.framework.ICommand;
import com.ote.mandate.business.model.Notary;
import lombok.Getter;

@Getter
public class DefineNotaryCommand extends BaseMandateCommand implements ICommand {

    private final Notary notary;

    public DefineNotaryCommand(String id, Notary notary) {
        super(id);
        this.notary = notary;
    }
}
