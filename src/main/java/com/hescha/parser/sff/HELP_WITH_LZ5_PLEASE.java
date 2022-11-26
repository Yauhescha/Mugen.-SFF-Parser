package com.hescha.parser.sff;

import com.hescha.parser.sff.parser.v2.decoder.ControlPacket;
import com.hescha.parser.sff.parser.v2.decoder.Lz5Packet;
import com.hescha.parser.sff.parser.v2.decoder.RlePacket;

import java.io.EOFException;
import java.io.IOException;
import java.io.RandomAccessFile;


public class HELP_WITH_LZ5_PLEASE {

    static int resIndex = 0;

    public static byte[] PARSE(int desiredLength, RandomAccessFile file) throws IOException {
        byte[] res = new byte[desiredLength];
        try {
            getBytes(desiredLength, file, res);

        } catch (EOFException ex) {
            System.out.println("End file");
        } catch (ArrayIndexOutOfBoundsException ex) {
            System.out.println("ArrayIndexOutOfBoundsException");
            ex.printStackTrace();
        }
        return res;
    }

    private static void getBytes(int desiredLength, RandomAccessFile file, byte[] res) throws IOException {
        resIndex = 0;
        System.out.println("Start decoding");
        // go throw all file
        while (true) {
            ControlPacket controlPacket = new ControlPacket(file);
            // go throw all packet by control flags
            for (int i = 0; i < controlPacket.flags.length; i++) {
                // check end of file
                if (resIndex == desiredLength) {
                    break;
                }

                System.out.println("Current resource index " + resIndex);

                // rle packet if 0
                if (controlPacket.flags[i] == 0) {
                    RlePacket rlePacket = new RlePacket(file);
                    for (int j = 0; j < rlePacket.count; j++)
                        res[resIndex++] = rlePacket.color;
                }

                // lz package
                else {
                    Lz5Packet lz5Packet = new Lz5Packet(file);
                    for (int j = 0; j < lz5Packet.copyLength; j++) {
                        byte mybyte = res[resIndex - lz5Packet.offset];
                        res[resIndex++] = mybyte;
                    }
//                    naivememcpy(res, lz5Packet.offset, lz5Packet.copyLength);
                }

            }


        }
    }

    private static void naivememcpy(byte[] dst, int offsetIndex, int copyLength) {
        for (int i = 0; i < copyLength; i++) {
            byte mybyte = dst[resIndex - offsetIndex];
            dst[resIndex++] = mybyte;
        }

    }

}

