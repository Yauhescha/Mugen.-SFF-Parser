package com.hescha.parser.sff.exception;

public class DecodeException extends RuntimeException {
    private static final String MESSAGE = "Wrong decode result. Expected length %d but actual is %d";

    public DecodeException(int expectedLength, int actualLength) {
        super(String.format(MESSAGE, expectedLength, actualLength));
    }
}
