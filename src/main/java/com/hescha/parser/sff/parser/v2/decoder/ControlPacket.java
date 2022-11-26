package com.hescha.parser.sff.parser.v2.decoder;

import java.io.IOException;
import java.io.RandomAccessFile;

public class ControlPacket {
    public byte[] flags;

    public ControlPacket(RandomAccessFile br) throws IOException {
        flags = new byte[8];

        var firstByte = br.readByte();

        flags[0] = (byte) ((firstByte & 0b00000001));
        flags[1] = (byte) ((firstByte & 0b00000010) >> 1);
        flags[2] = (byte) ((firstByte & 0b00000100) >> 2);
        flags[3] = (byte) ((firstByte & 0b00001000) >> 3);
        flags[4] = (byte) ((firstByte & 0b00010000) >> 4);
        flags[5] = (byte) ((firstByte & 0b00100000) >> 5);
        flags[6] = (byte) ((firstByte & 0b01000000) >> 6);
        flags[7] = (byte) ((firstByte & 0b10000000) >> 7);
    }
}