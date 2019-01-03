package com.ote.mandate.business.model.aggregate;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@NoArgsConstructor
@Data
public class Mandate {

    @NotBlank
    private String id;

    @NotBlank
    private String bankName;

    @NotNull
    @Valid
    private Contractor contractor;

    @Valid
    private Notary notary;

    @Valid
    private Heir mainHeir;

    @Valid
    private List<Heir> otherHeirs;

    public Mandate(String id, String bankName, Contractor contractor) {
        this.id = id;
        this.bankName = bankName;
        this.contractor = contractor;
    }
}
