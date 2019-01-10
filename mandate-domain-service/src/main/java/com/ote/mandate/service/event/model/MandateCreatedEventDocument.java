package com.ote.mandate.service.event.model;

import com.ote.mandate.business.aggregate.Mandate;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MandateCreatedEventDocument extends InnerEventDocument {

    private Mandate mandate;
}
