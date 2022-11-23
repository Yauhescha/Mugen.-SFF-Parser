package com.hescha.parser.sff.exception;

public class SignatureException extends RuntimeException {
    private static final String MESSAGE = "File with current signature does not supported";

    public SignatureException() {
        super(MESSAGE);
    }
}
