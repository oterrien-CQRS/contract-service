package com.ote.mandate.business.exception;

import com.ote.mandate.business.model.Heir;

import java.util.List;

public class MandateAllHeirAlreadyPresentException extends AdtMandateException {

    private final static String MESSAGE_TEMPLATE = "Mandate with id=%s does already contains heirs %s";

    public MandateAllHeirAlreadyPresentException(String id, List<Heir> heir) {
        super(String.format(MESSAGE_TEMPLATE, id, heir));
    }
}
