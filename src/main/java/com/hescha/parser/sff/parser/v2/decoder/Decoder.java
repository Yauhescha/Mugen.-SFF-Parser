package com.hescha.parser.sff.parser.v2.decoder;

import com.hescha.parser.sff.HELP_WITH_LZ5_PLEASE;
import com.hescha.parser.sff.exception.DecodeException;
import com.hescha.parser.sff.exception.NotSupportDecodeAlgorithmException;
import com.hescha.parser.sff.model.v2.SffV2Item;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
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
                return fromLZ5(data);
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

    private static byte[] fromLZ5(byte[] data) {
        int length = ByteBuffer.wrap(reverseArray(Arrays.copyOf(data, 4))).getInt();

        byte[] bytes = Arrays.copyOfRange(data, 4, data.length - 4);


        try {
            String newFIleName = "C:\\Users\\Administrator\\Desktop\\mugen\\"
                    + LocalDateTime.now().toEpochSecond(ZoneOffset.UTC) + "_" + Math.random();
            File file = new File(newFIleName);
            FileUtils.writeByteArrayToFile(file, bytes);

            RandomAccessFile r = new RandomAccessFile(file, "r");
            byte[] parse = HELP_WITH_LZ5_PLEASE.PARSE(length, r);
            r.close();
            file.delete();

            return parse;
        } catch (Exception e) {
            throw new RuntimeException(e);
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

    public static byte[] fromRLE8(byte[] data) {
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










//
//
//    public static byte[] tryy(byte[] rle){
//            if len(rle) == 0 {
//                return rle
//            }
//            p = make([]byte, int(s.Size[0])*int(s.Size[1]))
//            i, j, n := 0, 0, 0
//            ct, cts, rb, rbc := rle[i], uint(0), byte(0), uint(0)
//            if i < len(rle)-1 {
//                i++
//            }
//            for j < len(p) {
//                d := int(rle[i])
//                if i < len(rle)-1 {
//                    i++
//                }
//                if ct&byte(1<<cts) != 0 {
//                    if d&0x3f == 0 {
//                        d = (d<<2 | int(rle[i])) + 1
//                        if i < len(rle)-1 {
//                            i++
//                        }
//                        n = int(rle[i]) + 2
//                        if i < len(rle)-1 {
//                            i++
//                        }
//                    } else {
//                        rb |= byte(d & 0xc0 >> rbc)
//                        rbc += 2
//                        n = int(d & 0x3f)
//                        if rbc < 8 {
//                            d = int(rle[i]) + 1
//                            if i < len(rle)-1 {
//                                i++
//                            }
//                        } else {
//                            d = int(rb) + 1
//                            rb, rbc = 0, 0
//                        }
//                    }
//                    for {
//                        if j < len(p) {
//                            p[j] = p[j-d]
//                            j++
//                        }
//                        n--
//                        if n < 0 {
//                            break
//                        }
//                    }
//                } else {
//                    if d&0xe0 == 0 {
//                        n = int(rle[i]) + 8
//                        if i < len(rle)-1 {
//                            i++
//                        }
//                    } else {
//                        n = d >> 5
//                        d &= 0x1f
//                    }
//                    for ; n > 0; n-- {
//                        if j < len(p) {
//                            p[j] = byte(d)
//                                    j++
//                        }
//                    }
//                }
//                cts++
//                if cts >= 8 {
//                    ct, cts = rle[i], 0
//                    if i < len(rle)-1 {
//                        i++
//                    }
//                }
//            }
//            return
//    }

    private static int len(byte[] rle) {
        return rle.length;
    }


}
