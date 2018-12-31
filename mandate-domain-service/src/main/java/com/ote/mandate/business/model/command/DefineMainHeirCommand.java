package com.ote.mandate.business.model.command;

import com.ote.mandate.business.model.aggregate.Heir;
import lombok.Getter;

@Getter
public class DefineMainHeirCommand extends BaseMandateCommand {

    private Heir mainHeir;

    public DefineMainHeirCommand(String id, Heir mainHeir) {
        super(id);
        this.mainHeir = mainHeir;
    }
}
