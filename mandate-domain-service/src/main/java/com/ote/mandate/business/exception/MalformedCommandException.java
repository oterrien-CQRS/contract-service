package com.ote.mandate.business.exception;

import com.ote.framework.ICommand;


/**
 * To be used
 * https://www.baeldung.com/javax-validation
 */
public class MalformedCommandException extends Exception {
    public MalformedCommandException(ICommand command) {
        super("The command " + command.toString() + " is not well formed : TBD");
    }
}
