package com.hescha.parser.sff.exception;

import com.hescha.parser.sff.model.SffVersion;

public class VersionDoesNotSupportedException extends RuntimeException {
    private static final String MESSAGE = "Current version %s does not supported";

    public VersionDoesNotSupportedException(SffVersion version) {
        super(String.format(MESSAGE, version.getVerhi() + "."
                + version.getVerlo1()
                + version.getVerlo2()
                + version.getVerlo3()));
    }
}
