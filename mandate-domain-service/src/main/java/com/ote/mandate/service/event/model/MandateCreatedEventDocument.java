package com.ote.mandate.service.event.model;

import com.ote.mandate.business.model.aggregate.Mandate;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Getter
@Setter
@NoArgsConstructor
public class MandateCreatedEventDocument extends InnerEventDocument {

    private Mandate mandate;
}
