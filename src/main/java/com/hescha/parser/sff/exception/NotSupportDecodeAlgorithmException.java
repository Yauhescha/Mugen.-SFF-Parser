package com.hescha.parser.sff.exception;

public class NotSupportDecodeAlgorithmException extends RuntimeException {
    private static final String MESSAGE = "Current algorithm %s does not supported";

    public NotSupportDecodeAlgorithmException(String algorithmName) {
        super(String.format(MESSAGE, algorithmName));
    }
}
