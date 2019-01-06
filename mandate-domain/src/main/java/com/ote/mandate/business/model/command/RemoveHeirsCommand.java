package com.ote.mandate.business.model.command;

import com.ote.mandate.business.model.aggregate.Heir;
import lombok.Getter;
import lombok.ToString;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@ToString(callSuper = true)
public class RemoveHeirsCommand extends BaseMandateCommand {

    @NotEmpty
    @Valid
    private final Set<Heir> otherHeirs;

    public RemoveHeirsCommand(String id, Set<Heir> otherHeirs) {
        super(id);
        this.otherHeirs = otherHeirs;
    }

    public RemoveHeirsCommand(String id, Heir... otherHeirs) {
        this(id, Arrays.stream(otherHeirs).collect(Collectors.toSet()));
    }
}
