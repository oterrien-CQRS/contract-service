package com.ote.framework;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;
import java.util.stream.Collectors;

public interface Validable {

    default void validate() throws NotValidException {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            Validator validator = factory.getValidator();
            Set<ConstraintViolation<Validable>> violations = validator.validate(this);
            if (!violations.isEmpty()) {
                throw new NotValidException(this, violations);
            }
        }
    }

    class NotValidException extends Exception {

        private final static String MESSAGE_TEMPLATE = "The instance [%s] is not valid : %s";

        private NotValidException(Validable type, Set<ConstraintViolation<Validable>> violations) {
            super(String.format(MESSAGE_TEMPLATE, type.toString(), violations.stream().map(p -> p.getPropertyPath() + " " + p.getMessage()).collect(Collectors.toList())));
        }
    }
}
