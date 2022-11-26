package com.hescha.parser.sff.parser.v2.decoder;

import org.apache.commons.io.FileUtils;

import java.io.*;
import java.nio.ByteBuffer;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;

import static com.hescha.parser.sff.parser.v2.decoder.Decoder.reverseArray;


public class Lz5Decoder {

    public static byte[] decode(byte[] data) {
        int desiredLength = ByteBuffer.wrap(reverseArray(Arrays.copyOf(data, 4))).getInt();
        byte[] res = new byte[desiredLength];

        try {
            String newFIleName = "/mugenimages/"
                    + LocalDateTime.now().toEpochSecond(ZoneOffset.UTC)
                    + "_" + Math.random();
            File fileTemp = new File(newFIleName);
            FileUtils.writeByteArrayToFile(fileTemp, Arrays.copyOfRange(data, 4, data.length - 4));

            RandomAccessFile file = new RandomAccessFile(newFIleName, "r");
            getBytes(desiredLength, file, res);

        } catch (EOFException ex) {
            System.out.println("End file");
        } catch (ArrayIndexOutOfBoundsException ex) {
            System.out.println("ArrayIndexOutOfBoundsException");
            ex.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return res;
    }

    private static void getBytes(int desiredLength, RandomAccessFile file, byte[] res) throws IOException {
        int resIndex = 0;
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
                }
            }
        }
    }
}

