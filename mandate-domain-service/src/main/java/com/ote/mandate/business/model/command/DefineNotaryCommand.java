package com.ote.mandate.business.model.command;

import com.ote.mandate.business.model.aggregate.Notary;
import lombok.Getter;
import lombok.ToString;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Getter
@ToString(callSuper = true)
public class DefineNotaryCommand extends BaseMandateCommand {

    @NotNull
    @Valid
    private final Notary notary;

    public DefineNotaryCommand(String id, Notary notary) {
        super(id);
        this.notary = notary;
    }
}
