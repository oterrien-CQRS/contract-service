package com.ote.mandate.business.aggregate;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@NoArgsConstructor
@Data
public class Contractor {

    @NotBlank
    private String name;

    public Contractor(String name) {
        this.name = name;
    }
}
