package com.ote.mandate.business.aggregate;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@NoArgsConstructor
@Data
public class Notary {

    @NotBlank
    private String name;

    public Notary(String name) {
        this.name = name;
    }
}
