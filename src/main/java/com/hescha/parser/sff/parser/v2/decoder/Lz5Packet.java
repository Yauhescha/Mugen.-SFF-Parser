package com.hescha.parser.sff.parser.v2.decoder;

import java.io.IOException;
import java.io.RandomAccessFile;

public class Lz5Packet {
    public int offset;
    public int copyLength;

    public static int recycledCounter;
    public static int recycledByte;
    private static int counter = 0;

    public Lz5Packet(RandomAccessFile file) throws IOException {
        int byte1 = file.readUnsignedByte();
        //long package
        if ((byte1 & 0b00111111) == 0) {
            int top_offset = ((byte1 & 0xC0));
            int bottom8bitOffset = (file.readUnsignedByte());
            offset = (bottom8bitOffset + (top_offset << 2)) + 1;

            copyLength = file.readUnsignedByte() + 3;

        }
        //short package
        else {
            System.out.println("number iteration " + counter++);
            copyLength = (byte1 & 0x3F) + 1;
            recycledByte = (recycledByte << 2);
            recycledByte += (byte1 & 0xC0) >> 6;

            System.out.println("first byte; " + byte1 + ", "
                    + String.format("%8s", Integer.toBinaryString(byte1 & 0xFF).replace(' ', '0')));
            if (recycledCounter == 3) {
                offset = recycledByte + 1;
                System.out.println(String.format("SHORT-LZ:-USING-RECYCLED-OFFSET:_ %8s ",
                        Integer.toBinaryString(recycledByte & 0xFF)).replace(' ', '0')
                        + ", as digit: " + recycledByte);
                System.out.println();
                recycledByte = 0;
                recycledCounter = 0;
            } else {
                int secondByte = file.readUnsignedByte();
                offset = secondByte + 1;
                System.out.println("readed offset; " + secondByte + ", "
                        + String.format("%8s", Integer.toBinaryString(offset & 0xFF).replace(' ', '0')));
                System.out.println(String.format("SHORT-LZ:-READING-BYTE:_ %8s ",
                        Integer.toBinaryString(recycledByte & 0xFF)).replace(' ', '0')
                        + ", as digit: " + recycledByte);
                recycledCounter += 1;
            }
            System.out.println("offset: " + offset);
        }

    }
}