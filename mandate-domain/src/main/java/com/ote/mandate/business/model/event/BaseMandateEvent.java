package com.ote.mandate.business.model.event;

import com.ote.framework.IEvent;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@NoArgsConstructor
@Getter
@Setter
@ToString
abstract class BaseMandateEvent implements IEvent, Serializable {

    @NotNull
    @NotEmpty
    private String id;

    public BaseMandateEvent(String id) {
        this.id = id;
    }
}
