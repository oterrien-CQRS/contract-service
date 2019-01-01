package com.ote.mandate.business.exception;

import com.ote.framework.Validable;

public class MalformedCommandException extends Exception {
    public MalformedCommandException(Validable.NotValidException e) {
        super(e.getMessage());
    }
}
