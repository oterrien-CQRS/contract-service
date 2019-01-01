package com.ote.mandate.service.rest.payload;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class NotaryPayload {

    @NotBlank
    private String name;
}
