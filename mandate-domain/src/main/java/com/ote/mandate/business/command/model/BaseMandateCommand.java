package com.ote.mandate.business.command.model;

import com.ote.common.cqrs.ICommand;
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
