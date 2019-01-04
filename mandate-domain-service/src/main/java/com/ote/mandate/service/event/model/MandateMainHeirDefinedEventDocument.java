package com.ote.mandate.service.event.model;

import com.ote.mandate.business.model.aggregate.Heir;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MandateMainHeirDefinedEventDocument extends InnerEventDocument {

    private Heir heir;
}
