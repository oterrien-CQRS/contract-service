package com.ote.mandate.service.event.model;

import com.ote.mandate.business.aggregate.Heir;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
