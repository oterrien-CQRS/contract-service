package com.ote.mandate.service.event.model;

import com.ote.mandate.business.model.aggregate.Notary;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MandateNotaryDefinedEventDocument extends InnerEventDocument {

    private Notary notary;
}
