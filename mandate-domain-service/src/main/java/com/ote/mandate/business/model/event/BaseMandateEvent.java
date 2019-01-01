package com.ote.mandate.business.model.event;

import com.ote.framework.IEvent;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@RequiredArgsConstructor
@Getter
@ToString
public abstract class BaseMandateEvent implements IEvent {

    @NotNull
    @NotEmpty
    private final String id;
}
