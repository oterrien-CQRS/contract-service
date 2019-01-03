package com.ote.mandate.business.exception;

public class MandateAlreadyCreatedException extends AdtMandateException {

    private final static String MESSAGE_TEMPLATE = "MandatePayload with id=%s has already been created";

    public MandateAlreadyCreatedException(String id) {
        super(String.format(MESSAGE_TEMPLATE, id));
    }
}
