package com.hescha.parser.sff.exception;

public class VersionDoesNotSupportedException extends RuntimeException{
    private static final String MESSAGE = "Version greater than 1 does not supported";
    public VersionDoesNotSupportedException() {
        super(MESSAGE);
    }
}
