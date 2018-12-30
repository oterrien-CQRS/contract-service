package com.ote.mandate.business.exception;

import com.ote.mandate.business.model.Heir;

public class MandateMainHeirIsAlreadyDefinedException extends AdtMandateException {

    private final static String MESSAGE_TEMPLATE = "Mandate with id=%s does already have main heir %s";

    public MandateMainHeirIsAlreadyDefinedException(String id, Heir mainHeir) {
        super(String.format(MESSAGE_TEMPLATE, id, mainHeir));
    }
}
