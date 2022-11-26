package com.hescha.parser.sff.parser.v2.decoder;

import java.io.IOException;
import java.io.RandomAccessFile;

public class RlePacket {
    public byte color;
    public int count;

    public RlePacket(RandomAccessFile file) throws IOException {
        int byte1 = file.readUnsignedByte();
        color = (byte) (byte1 & 0b00011111);

        // long rle
        if ((byte1 & 0b11100000) == 0) {
            count = file.readUnsignedByte() + 8;
        }// short rle
        else {
            count = ((byte1 & 0b11100000) >> 5);
        }
    }
}