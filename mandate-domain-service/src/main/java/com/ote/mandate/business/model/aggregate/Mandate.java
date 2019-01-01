package com.ote.mandate.business.model.aggregate;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Data
public class Mandate {

    @NotBlank
    private final String id;

    @NotBlank
    private String bankName;

    @NotNull
    private Contractor contractor;

    @Valid
    private Notary notary;

    @Valid
    private Heir mainHeir;

    @Valid
    private final List<Heir> otherHeirs = new ArrayList<>();
}
