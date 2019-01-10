package com.ote.mandate.business.command.model;

import com.ote.mandate.business.aggregate.Contractor;
import com.ote.mandate.business.aggregate.Heir;
import com.ote.mandate.business.aggregate.Notary;
import lombok.Getter;
import lombok.ToString;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
@ToString(callSuper = true)
public class CreateMandateCommand extends BaseMandateCommand {

    @NotBlank
    @Valid
    private final String bankName;

    @NotNull
    @Valid
    private final Contractor contractor;

    @Valid
    private final Notary notary;

    @Valid
    private final Heir mainHeir;

    @Valid
    private final Set<Heir> otherHeirs;

    public CreateMandateCommand(String id, String bankName, Contractor contractor, Notary notary, Heir mainHeir, Set<Heir> otherHeirs) {
        super(id);
        this.bankName = bankName;
        this.contractor = contractor;
        this.notary = notary;
        this.mainHeir = mainHeir;
        this.otherHeirs = otherHeirs;
    }

    public CreateMandateCommand(String id, String bankName, Contractor contractor, Notary notary, Heir mainHeir, Heir... otherHeirs) {
        this(id, bankName, contractor, notary, mainHeir, Stream.of(otherHeirs).collect(Collectors.toSet()));
    }
}
