package com.ote.mandate.business.model.aggregate;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Data
public class Mandate {

    private final String id;
    private String bankName;
    private Contractor contractor;
    private Notary notary;
    private Heir mainHeir;
    private final List<Heir> otherHeirs = new ArrayList<>();


}
