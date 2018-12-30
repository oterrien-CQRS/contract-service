package com.ote.mandate.business.model.command;

import com.ote.framework.ICommand;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public abstract class BaseMandateCommand implements ICommand {

    private final String id;
}
