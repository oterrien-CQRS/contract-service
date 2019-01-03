package com.ote.mandate.business.model.aggregate;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@NoArgsConstructor
@Data
public class Heir {

    @NotBlank
    private String name;

    public Heir(String name) {
        this.name = name;
    }
}
