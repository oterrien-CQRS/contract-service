package com.ote.mandate.business.model.command;

import com.ote.mandate.business.model.aggregate.Heir;
import lombok.Getter;
import lombok.ToString;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Getter
@ToString(callSuper = true)
public class DefineMainHeirCommand extends BaseMandateCommand {

    @NotNull
    @Valid
    private final Heir mainHeir;

    public DefineMainHeirCommand(String id, Heir mainHeir) {
        super(id);
        this.mainHeir = mainHeir;
    }
}
