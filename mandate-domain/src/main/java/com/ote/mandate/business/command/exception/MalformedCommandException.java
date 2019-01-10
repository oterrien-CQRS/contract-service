package com.ote.mandate.business.command.exception;


import com.ote.common.Validable;

public class MalformedCommandException extends Exception {
    public MalformedCommandException(Validable.NotValidException e) {
        super(e.getMessage());
    }
}
