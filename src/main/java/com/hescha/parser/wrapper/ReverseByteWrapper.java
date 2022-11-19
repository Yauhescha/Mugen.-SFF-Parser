package com.hescha.parser.wrapper;

import java.nio.ByteBuffer;

public class ReverseByteWrapper {
    public static int getInt(byte[] array) {
        return ByteBuffer.wrap(reverseArray(array)).getInt();
    }

    private static byte[] reverseArray(byte[] a) {
        byte[] ret = new byte[a.length];
        for (int i = 0, j = a.length - 1; i < a.length && j >= 0; i++, j--)
            ret[i] = a[j];
        return ret;
    }
}
