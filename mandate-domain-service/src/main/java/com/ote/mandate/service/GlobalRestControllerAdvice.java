package com.ote.mandate.service;

import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalRestControllerAdvice {

    private static final String MESSAGE_TEMPLATE = "The instance [%s] is not valid : %s";

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handle(MethodArgumentNotValidException exception) {
        exception.printStackTrace();
        BindingResult result = exception.getBindingResult();
        return String.format(MESSAGE_TEMPLATE, result.getTarget().toString(), result.getFieldErrors().stream().map(p -> p.getField() + " " + p.getDefaultMessage()).collect(Collectors.toList()));
    }

    @ExceptionHandler(Throwable.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handle(Throwable exception) {
        exception.printStackTrace();
        return exception.toString();
    }
}
