package com.ote.mandate.business.spi;

import com.ote.mandate.business.model.Mandate;

import java.util.Optional;

public interface IMandateRepository {

    Optional<Mandate> find(String id);

    void save(Mandate mandate);
}
