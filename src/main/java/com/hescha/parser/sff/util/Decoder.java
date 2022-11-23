package com.hescha.parser.sff.util;

import com.hescha.parser.sff.exception.DecodeException;
import com.hescha.parser.sff.exception.NotSupportDecodeAlgorithmException;
import com.hescha.parser.sff.model.v2.SffV2Item;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Decoder {

    //, когда это 3, это означает, что он сжат алгоритмом Rle5,
    //когда он равен 4, это означает, что он сжат алгоритмом Lz5,
    //когда он равен 10, это означает, что он сжат алгоритмом PNG8
    //до 11, это означает, что когда используется алгоритм PNG24 для сжатия
    //до 12 это означает, что алгоритм PNG32 используется для сжатия
    public static byte[] decode(SffV2Item sprite, byte[] data) {
        switch (sprite.getCompressionAlgorithm()) {
            case 0:
                return data;
            case 2:
                return fromRLE8(data);
            case 3:
                return fromRLE5(data);
            case 4:
//                return fromLZ5(data);
            case 10:
//                return fromPNG8(data);
            case 11:
//                return fromPNG24(data);
            case 12:
//                return fromPNG32(data);
            default:
                // TODO: change exception constructor with enum
                throw new NotSupportDecodeAlgorithmException(sprite.getCompressionAlgorithm() + "");
        }
    }

    private static byte[] fromRLE5(byte[] data) {
        List<Byte> result = new ArrayList<>();
        int length = ByteBuffer.wrap(reverseArray(Arrays.copyOf(data, 4))).getInt();
        int byteIndex = 4;
        byte color;

        RLE5packet rle5packet = new RLE5packet(byteIndex, data);
        byteIndex += 2;

        if (rle5packet.isColorBitOne()) {
            color = data[byteIndex];
            byteIndex++;
        } else {
            color = 0;
        }

        for (byte run_count = 0; run_count < rle5packet.getRunLength(); run_count++) {
            result.add(color);
        }

        // Decode 3RL/5VAL
        for (byte bytes_processed = 0; bytes_processed < rle5packet.getDataLength() - 1; bytes_processed++) {
            byte one_byte = data[byteIndex];
            byteIndex++;
            color = (byte) (one_byte & 0x1F);
            int run_length = one_byte >> 5;
            for (int run_count = 0; run_count < run_length; run_count++) {
                result.add(color);
            }
        }

        if (length != result.size()) {
            System.out.println("fromRLE5");
            throw new DecodeException(length, result.size());
        }
        return getBytesFromList(result);

    }

    private static byte[] fromRLE8(byte[] data) {
        List<Byte> result = new ArrayList<>();
        int length = ByteBuffer.wrap(reverseArray(Arrays.copyOf(data, 4))).getInt();

        for (int i = 4; i < data.length; i++) {
            byte temp = data[i];
            if ((temp & 0xC0) == 0x40) {
                i++;
                int count = (temp & 0x3F);
                byte color = data[i];
                for (int j = 0; j < count; j++) {
                    result.add(color);
                }

            } else {
                result.add(temp);
            }
        }
        if (length != result.size()) {
            System.out.println("fromRLE8");
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

class RLE5packet {
    byte byte1;
    byte byte2;
    byte dataLength;

    public RLE5packet(int startPoint, byte[] data) {
        this.byte1 = data[startPoint];
        this.byte2 = data[startPoint + 1];
        this.dataLength = (byte) (byte2 & 01111111);
    }

    public boolean isColorBitOne() {
        return (byte2 & 10000000) == 10000000;
    }

    public byte getRunLength() {
        return byte1;
    }

    public byte getDataLength() {
        return dataLength;
    }
}
