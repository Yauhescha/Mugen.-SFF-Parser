package com.hescha.parser.sff.util;

import com.hescha.parser.sff.exception.DecodeException;
import com.hescha.parser.sff.exception.NotSupportDecodeAlgorithmException;
import com.hescha.parser.sff.model.v2.SffV2Item;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Decoder {

    public static byte[] decode(SffV2Item sprite, byte[] data) {
        switch (sprite.getCompressionAlgorithm()) {
            case 0:
                return data;
            case 2:
                return fromRLE8(data);
            default:
                // TODO: change exception constructor with enum
                throw new NotSupportDecodeAlgorithmException(sprite.getCompressionAlgorithm() + "");
        }
    }

    private static byte[] fromRLE8(byte[] arr) {
        List<Byte> result = new ArrayList<>();
        int length = ByteBuffer.wrap(reverseArray(Arrays.copyOf(arr, 4))).getInt();

        for (int i = 4; i < arr.length; i++) {
            byte temp = arr[i];
            if ((temp & 0xC0) == 0x40) {
                i++;
                int count = (temp & 0x3F);
                byte color = arr[i];
                for (int j = 0; j < count; j++) {
                    result.add(color);
                }

            } else {
                result.add(temp);
            }
        }
        if (length != result.size()) {
            throw new DecodeException(length, result.size());
        }

        return getBytesFromList(result);
    }
    // TODO: try to find better way
    private static byte[] getBytesFromList(List<Byte> result) {
        byte[] byteRes = new byte[result.size()];
        for (int i = 0; i < result.size(); i++) {
            byteRes[i] = result.get(i);
        }
        return byteRes;
    }

    // TODO: try to find better way
    private static byte[] reverseArray(byte[] a) {
        byte[] ret = new byte[a.length];
        for (int i = 0, j = a.length - 1; i < a.length && j >= 0; i++, j--)
            ret[i] = a[j];
        return ret;
    }
}
