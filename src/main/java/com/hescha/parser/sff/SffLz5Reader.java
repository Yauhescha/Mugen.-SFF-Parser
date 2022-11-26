package com.hescha.parser.sff;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SffLz5Reader {
    public List<Byte> ReadAsync(String filename) throws Exception {
        var ret = new ArrayList<Byte>();
        var binaryReader = new RandomAccessFile(filename, "r");
        var lz5ControlPacket = new Lz5ControlPacket();
        var lz5RlePacket = new Lz5RlePacket();
        var lz5LzPacket = new Lz5LzPacket();
        var tmp = new ArrayList<Byte>();

        while (binaryReader.getFilePointer() < binaryReader.length()) {
            lz5ControlPacket.fromStream(binaryReader);

            for (var flag = 0; flag < lz5ControlPacket.Flags.length; flag++) {
                long filePointer = binaryReader.getFilePointer();
                long length = binaryReader.length();
                if (filePointer == length|| filePointer>length) {
                    break;
                }

                if (lz5ControlPacket.Flags[flag] == 0) {
                    lz5RlePacket.fromStream(binaryReader);

                    for (var i = 0; i < lz5RlePacket.Count; i++) {
                        ret.add(lz5RlePacket.Color);
                    }
                } else if (lz5ControlPacket.Flags[flag] == 1) {
                    lz5LzPacket.fromStream(binaryReader);

                    for (var i = 0; i < lz5LzPacket.Length; i++) {
                        var off = ret.size() - lz5LzPacket.Offset + i;

                        if (off < ret.size()) {
                            if(off<0)break;
                            tmp.add(ret.get(off));
                        } else {
                            break;
                        }
                    }

                    if (tmp.size() < lz5LzPacket.Length && tmp.size() != 0) {
                        var count = tmp.size();
                        var len = (lz5LzPacket.Length - count) / count;
                        var tmp2 = List.copyOf(tmp);

                        for (var i = 0; i < len; i++) {
                            tmp.addAll(tmp2);
                        }

                        tmp.addAll(tmp2.subList(0, (lz5LzPacket.Length - count) % count));
                    }

                    ret.addAll(tmp);
                }
            }
        }

        binaryReader.close();

        return ret;
    }

}

class Lz5ControlPacket {
    public byte[] Flags;

    public void fromStream(RandomAccessFile br) throws IOException {
        Flags = new byte[8];

        var firstByte = br.readByte();

        Flags[7] = (byte) ((firstByte & 128) / 128);
        Flags[6] = (byte) ((firstByte & 64) / 64);
        Flags[5] = (byte) ((firstByte & 32) / 32);
        Flags[4] = (byte) ((firstByte & 16) / 16);
        Flags[3] = (byte) ((firstByte & 8) / 8);
        Flags[2] = (byte) ((firstByte & 4) / 4);
        Flags[1] = (byte) ((firstByte & 2) / 2);
        Flags[0] = (byte) (firstByte & 1);
    }
}

class Lz5RlePacket {
    public byte Color;
    public int Count;

    public void fromStream(RandomAccessFile br) throws IOException {
        var b = br.readByte();
        Count = (b & 224) >> 5;

        if (Count == 0) {
            var b2 = br.readByte();
            Count = b2 + 8;
        }

        Color = (byte) (b & 31);
    }
}

class Lz5LzPacket {
    public int Length;

    public int Offset;
    public byte Recycled;
    public byte RecycledBitsFilled;
    public void fromStream(RandomAccessFile br) throws IOException {
        var b = br.readByte();
        byte b2, b3, tmp;
        Length = b & 63;

        if (Length == 0) {
            b2 = br.readByte();
            b3 = br.readByte();
            Offset = (b & 192) * 4 + b2 + 1;
            Length = b3 + 3;
        } else {
            Length++;
            tmp = (byte) (b & 192);

            if (RecycledBitsFilled == 2) {
                tmp >>= 2;
            }

            if (RecycledBitsFilled == 4) {
                tmp >>= 4;
            }

            if (RecycledBitsFilled == 6) {
                tmp >>= 6;
            }

            Recycled += tmp;
            RecycledBitsFilled += 2;

            if (RecycledBitsFilled < 8) {
                b2 = br.readByte();
                Offset = b2;
            } else if (RecycledBitsFilled == 8) {
                Offset = Recycled;
                Recycled = 0;
                RecycledBitsFilled = 0;
            }

            Offset += 1;
        }
    }
}