package com.ote.mandate.business.command.model;

import com.ote.mandate.business.aggregate.Notary;
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
