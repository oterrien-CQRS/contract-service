package com.ote.mandate.service.rest.payload;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class ContractorPayload {

    @NotBlank
    private String name;
}
