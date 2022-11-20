package com.hescha.parser.sff.parser;

import com.hescha.parser.sff.exception.SignatureException;
import com.hescha.parser.sff.model.v1.SffHeader;

import java.io.IOException;
import java.io.RandomAccessFile;

public abstract class SffHeaderParser {

    protected void checkSignature(RandomAccessFile accessFile) throws IOException {
        accessFile.seek(0);
        byte[] signature = new byte[11];
        accessFile.read(signature);
        if (!SffHeader.signature.equals(new String(signature))) {
            throw new SignatureException();
        }
    }
}
