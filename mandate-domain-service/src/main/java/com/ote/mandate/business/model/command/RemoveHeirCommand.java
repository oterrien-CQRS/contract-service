package com.ote.mandate.business.model.command;

import com.ote.mandate.business.model.aggregate.Heir;
import lombok.Getter;

import java.util.List;

@Getter
public class RemoveHeirCommand extends BaseMandateCommand {

    private final List<Heir> otherHeirs;

    public RemoveHeirCommand(String id, List<Heir> otherHeirs) {
        super(id);
        this.otherHeirs = otherHeirs;
    }
}
