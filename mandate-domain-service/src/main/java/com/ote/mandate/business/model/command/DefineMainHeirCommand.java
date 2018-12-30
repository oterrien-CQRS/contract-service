package com.ote.mandate.business.model.command;

import com.ote.framework.ICommand;
import com.ote.mandate.business.model.Heir;
import lombok.Getter;

@Getter
public class DefineMainHeirCommand extends BaseMandateCommand implements ICommand {

    private Heir mainHeir;

    public DefineMainHeirCommand(String id, Heir mainHeir) {
        super(id);
        this.mainHeir = mainHeir;
    }
}
