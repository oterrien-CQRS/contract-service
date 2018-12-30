package com.ote.mandate.business.exception;

import com.ote.mandate.business.model.Notary;

public class MandateNotaryIsAlreadyDefinedException extends AdtMandateException {

    private final static String MESSAGE_TEMPLATE = "Mandate with id=%s does already have notary %s";

    public MandateNotaryIsAlreadyDefinedException(String id, Notary notary) {
        super(String.format(MESSAGE_TEMPLATE, id, notary));
    }
}
