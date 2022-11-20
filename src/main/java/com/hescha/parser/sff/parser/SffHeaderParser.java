package com.hescha.parser.sff.parser;

import com.hescha.parser.sff.exception.SignatureException;
import com.hescha.parser.sff.exception.VersionDoesNotSupportedException;
import com.hescha.parser.sff.model.SffVersion;
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

    protected SffVersion readVersion(RandomAccessFile accessFile, int wantedVersion) throws IOException {
        accessFile.seek(12);
        byte verhi = accessFile.readByte();
        byte verlo1 = accessFile.readByte();
        byte verlo2 = accessFile.readByte();
        byte verlo3 = accessFile.readByte();
        SffVersion sffVersion = new SffVersion(verhi, verlo1, verlo2, verlo3);
        if (verlo3 != wantedVersion) {
            throw new VersionDoesNotSupportedException(sffVersion);
        }
        return sffVersion;
    }
}
