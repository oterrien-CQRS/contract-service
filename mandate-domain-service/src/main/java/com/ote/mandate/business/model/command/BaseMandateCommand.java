package com.ote.mandate.business.model.command;

import com.ote.framework.ICommand;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@RequiredArgsConstructor
@Getter
@ToString
public abstract class BaseMandateCommand implements ICommand {

    @NotBlank
    private final String id;
}
