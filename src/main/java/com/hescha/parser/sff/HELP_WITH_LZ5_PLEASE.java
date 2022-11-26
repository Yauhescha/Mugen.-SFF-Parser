package com.hescha.parser.sff;

import java.io.EOFException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.List;


public class HELP_WITH_LZ5_PLEASE {

    static int resIndex = 0;
    public static byte[] PARSE(int desiredLength, RandomAccessFile file) throws IOException {
        byte[] res = new byte[desiredLength];
        try {
            getBytes(desiredLength, file, res);

        }catch (EOFException ex){
            System.out.println("End file");
        }catch (ArrayIndexOutOfBoundsException ex){
            System.out.println("ArrayIndexOutOfBoundsException");
        }
        return res;
    }

    private static void getBytes(int desiredLength, RandomAccessFile file, byte[] res) throws IOException {
        resIndex = 0;
        System.out.println("Start program");
        // go throw all file
        while (true) {
            ControlPacket controlPacket = new ControlPacket(file);
            // go throw all packet by control flags
            for (int i = 0; i < controlPacket.flags.length; i++) {
                // check end of file
                if (resIndex == desiredLength) {
                    break;
                }

                System.out.println("Current resource index "+resIndex);

                // rle packet if 0
                if (controlPacket.flags[i] == 0) {
                    RlePacket rlePacket = new RlePacket(file);
                    for (int j = 0; j < rlePacket.count; j++)
                        res[resIndex++] = rlePacket.color;
                }

                // lz package
                else {
                    Lz5Packet lz5Packet = new Lz5Packet(file);
                    naivememcpy(res, lz5Packet.offset, lz5Packet.copyLength);
                }

            }


        }
    }

    private static void naivememcpy(byte[] dst, int offsetIndex, int copyLength) {
        for(int i=0; i<copyLength; i++){
            byte mybyte =dst[resIndex - offsetIndex ];
            dst[resIndex++]=mybyte;
        }

    }

}

class Lz5Packet {
    public int offset;
    public int copyLength;

    public static int recycledCounter;
    public static int recycledByte;
    private static int counter=0;

    public Lz5Packet(RandomAccessFile file) throws IOException {
        int byte1 = file.readUnsignedByte();
        //long package
        if ((byte1 & 0b00111111) == 0) {
            int top_offset = ((byte1 & 0xC0));
            int bottom8bitOffset = (file.readUnsignedByte());
            offset = (bottom8bitOffset + (top_offset << 2))+1;

            copyLength = file.readUnsignedByte() + 3;

        }
        //short package
        else {
            System.out.println("number iteration "+counter++);
            copyLength = (byte1 & 0x3F) + 1;
            recycledByte =  (recycledByte << 2);
            recycledByte += (byte1 & 0xC0) >> 6;

            System.out.println("first byte; " + byte1+", "
                    + String.format("%8s",Integer.toBinaryString(byte1 & 0xFF).replace(' ', '0')));
            if (recycledCounter == 3) {
                offset = recycledByte + 1;
                System.out.println(String.format("SHORT-LZ:-USING-RECYCLED-OFFSET:_ %8s ",
                        Integer.toBinaryString(recycledByte & 0xFF)).replace(' ', '0')
                +", as digit: " + recycledByte);
                System.out.println();
                recycledByte = 0;
                recycledCounter = 0;
            } else {
                int secondByte = file.readUnsignedByte();
                offset = secondByte + 1;
                System.out.println("readed offset; " + secondByte+", "
                        + String.format("%8s",Integer.toBinaryString(offset & 0xFF).replace(' ', '0')));
                System.out.println(String.format("SHORT-LZ:-READING-BYTE:_ %8s ",
                        Integer.toBinaryString(recycledByte & 0xFF)).replace(' ', '0')
                        +", as digit: " + recycledByte);
                recycledCounter += 1;
            }
            System.out.println("offset: " + offset);
            // 1 - 01000100, my: 01000100
            // 2 - 01001100, my: 00111100
            // 3 - 11010100, my: 01000011
        }

    }
}

class RlePacket {
    public byte color;
    public int count;

    public RlePacket(RandomAccessFile file) throws IOException {
        var byte1 = file.readUnsignedByte();
        color = (byte) (byte1 & 0b00011111);

        // long rle
        if ((byte1 & 0b11100000) == 0) {
            count = file.readUnsignedByte() + 8;
        } else {
            count = ((byte1 & 0b11100000) >> 5);
        }

    }
}

class ControlPacket {
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