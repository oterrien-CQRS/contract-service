package com.ote.mandate.service.rest.payload;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Data
public class MandatePayload {

    @NotBlank
    private String id;

    @NotBlank
    private String bankName;

    @NotNull
    @Valid
    private ContractorPayload contractor;

    @Valid
    private NotaryPayload notary;

    @Valid
    private HeirPayload mainHeir;

    @Valid
    private List<HeirPayload> otherHeirs = new ArrayList<>();
}
