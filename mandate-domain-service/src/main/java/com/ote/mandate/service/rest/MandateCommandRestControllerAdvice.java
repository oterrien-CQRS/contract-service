package com.ote.mandate.service.rest;

import com.ote.mandate.business.command.exception.MalformedCommandException;
import com.ote.mandate.business.command.exception.MandateAlreadyCreatedException;
import com.ote.mandate.business.command.exception.MandateNotYetCreatedException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice("com.ote.mandate.service.rest")
@ConditionalOnClass(MandateCommandController.class)
public class MandateCommandRestControllerAdvice {

    @ExceptionHandler(MandateAlreadyCreatedException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handle(MandateAlreadyCreatedException exception) {
        exception.printStackTrace();
        return exception.getMessage();
    }

    @ExceptionHandler(MandateNotYetCreatedException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handle(MandateNotYetCreatedException exception) {
        exception.printStackTrace();
        return exception.getMessage();
    }

    @ExceptionHandler(MalformedCommandException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handle(MalformedCommandException exception) {
        exception.printStackTrace();
        return exception.getMessage();
    }
}
