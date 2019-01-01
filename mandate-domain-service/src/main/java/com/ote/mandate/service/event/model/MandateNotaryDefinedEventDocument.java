package com.ote.mandate.service.event.model;

import com.ote.mandate.business.model.aggregate.Notary;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Getter
@Setter
@NoArgsConstructor
public class MandateNotaryDefinedEventDocument extends InnerEventDocument {

    private Notary notary;
}
