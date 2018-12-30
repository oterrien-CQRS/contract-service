package com.ote.mandate.business.exception;

public class MandateNotYetCreatedException extends AdtMandateException {

    private final static String MESSAGE_TEMPLATE = "Mandate with id=%s has not yet been created";

    public MandateNotYetCreatedException(String id) {
        super(String.format(MESSAGE_TEMPLATE, id));
    }
}
