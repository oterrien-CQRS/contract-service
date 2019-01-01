package com.ote.mandate.service.event.model;

import com.ote.mandate.business.model.aggregate.Heir;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Getter
@Setter
@NoArgsConstructor
public class MandateOtherHeirUpdatedEventDocument extends InnerEventDocument {

    private Heir heir;

    private Action action;

    public enum Action {
        ADDED, DELETED
    }
}
