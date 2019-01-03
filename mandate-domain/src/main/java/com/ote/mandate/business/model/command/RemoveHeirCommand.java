package com.ote.mandate.business.model.command;

import com.ote.mandate.business.model.aggregate.Heir;
import lombok.Getter;
import lombok.ToString;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Getter
@ToString(callSuper = true)
public class RemoveHeirCommand extends BaseMandateCommand {

    @NotEmpty
    @Valid
    private final List<Heir> otherHeirs;

    public RemoveHeirCommand(String id, List<Heir> otherHeirs) {
        super(id);
        this.otherHeirs = otherHeirs;
    }
}
