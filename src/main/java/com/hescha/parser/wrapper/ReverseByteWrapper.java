package com.hescha.parser.wrapper;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;

public class ReverseByteWrapper {
    public static int readShort(RandomAccessFile file) throws IOException {
        byte[] sizeOfSubheaderInBytes = new byte[2];
        file.read(sizeOfSubheaderInBytes);
        return ByteBuffer.wrap(reverseArray(sizeOfSubheaderInBytes)).getShort();
    }

    public static int readInt(RandomAccessFile file) throws IOException {
        byte[] sizeOfSubheaderInBytes = new byte[4];
        file.read(sizeOfSubheaderInBytes);
        return ByteBuffer.wrap(reverseArray(sizeOfSubheaderInBytes)).getInt();
    }

    private static byte[] reverseArray(byte[] a) {
        byte[] ret = new byte[a.length];
        for (int i = 0, j = a.length - 1; i < a.length && j >= 0; i++, j--)
            ret[i] = a[j];
        return ret;
    }

}
