package com.ote.common;

/**
 * Used to catch any exception from a lambda execution
 * Throw this exception instead of the inner one and catch it after
 * Example:
 * <p>
 * try {
 * events.forEach(event -> {
 * try {
 * eventHandler.handle(event);
 * } catch (Exception e) {
 * throw new LambdaException(e);
 * }
 * });
 * } catch (LambdaException e) {
 * throw e.getCause();
 * }
 */
public class LambdaException extends RuntimeException {
    public LambdaException(Exception e) {
        super(e);
    }
}