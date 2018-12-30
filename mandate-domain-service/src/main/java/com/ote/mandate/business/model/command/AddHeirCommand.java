package com.ote.mandate.business.model.command;

import com.ote.mandate.business.model.Heir;
import lombok.Getter;

import java.util.List;

@Getter
public class AddHeirCommand extends BaseMandateCommand {

    private final List<Heir> otherHeirs;

    public AddHeirCommand(String id, List<Heir> otherHeirs) {
        super(id);
        this.otherHeirs = otherHeirs;
    }
}
