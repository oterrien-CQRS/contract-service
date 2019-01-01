package com.ote.mandate.business.model.aggregate;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotBlank;

@RequiredArgsConstructor
@Data
public class Notary {

    @NotBlank
    private final String name;
}
