package com.hescha.parser.sff.parser.v2;

import com.hescha.parser.sff.model.SffVersion;
import com.hescha.parser.sff.model.v2.SffV2Header;
import com.hescha.parser.sff.parser.SffHeaderParser;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

import static com.hescha.parser.wrapper.ReverseByteWrapper.readInt;

/*--| SFF file structure
|--------------------------------------------------*\
Version 1.01
HEADER (512 bytes)
------
Bytes
0    0    12   "ElecbyteSpr\0" signature
12    C     1   verlo3; 0
13    D     1   verlo2; 0
14    E     1   verlo1; 0
15    F     1   verhi; 2
16   10     4   reserved; 0
20   14     4   reserved; 0
24   18     1   compatverlo3; 0
25   19     1   compatverlo1; 0
26   1A     1   compatverlo2; 0
27   1B     1   compatverhi; 2
28   1C     4   reserved; 0
32   20     4   reserved; 0
36   24     4   offset where first sprite node header data is located
40   28     4   Total number of sprites
44   2C     4   offset where first palette node header data is located
48   30     4   Total number of palettes
52   34     4   ldata offset
56   38     4   ldata length
60   3C     4   tdata offset
64   40     4   tdata length
68   44     4   reserved; 0
72   48     4   reserved; 0
76   4C   436   unused
\*--------------------------------------------------------------------------*/
public class SffV2HeaderParser extends SffHeaderParser {

    private RandomAccessFile accessFile;

    public SffV2Header parse(File file) throws IOException {
        accessFile = new RandomAccessFile(file, "r");
        checkSignature(accessFile);

        SffVersion version = readVersion(accessFile, 2);
        SffVersion compatVersion = readCompatVersion();
        int offsetFirstSpriteNode = readOffsetFirstSpriteNode();
        int numberOfSprites = readNumberOfSprites();
        int offsetFirstPaletteNode =readOffsetFirstPaletteNode();
        int numberOfPalettes = readNumberOfPalettes();
        int lDataOffset = readLDataOffset();
        int lDataLength = readLDataLength();
        int tDataOffset = readTDataOffset();
        int tDataLength = readTDataLength();
        String comment = readComment();

        accessFile.close();

        return SffV2Header.builder()
                .version(version)
                .compatVersion(compatVersion)
                .offsetFirstSpriteNode(offsetFirstSpriteNode)
                .numberOfSprites(numberOfSprites)
                .offsetFirstPaletteNode(offsetFirstPaletteNode)
                .numberOfPalettes(numberOfPalettes)
                .lDataOffset(lDataOffset)
                .lDataLength(lDataLength)
                .tDataOffset(tDataOffset)
                .tDataLength(tDataLength)
                .comment(comment)
                .build();
    }

    protected SffVersion readCompatVersion() throws IOException {
        accessFile.seek(24);
        byte compatverlo3 = accessFile.readByte();
        byte compatverlo1 = accessFile.readByte();
        byte compatverlo2 = accessFile.readByte();
        byte compatverhi = accessFile.readByte();
        return new SffVersion(compatverhi, compatverlo1, compatverlo2, compatverlo3);
    }

    private int readOffsetFirstSpriteNode() throws IOException {
        accessFile.seek(36);
        return readInt(accessFile);
    }

    private int readNumberOfSprites() throws IOException {
        accessFile.seek(40);
        return readInt(accessFile);
    }

    private int readOffsetFirstPaletteNode() throws IOException {
        accessFile.seek(44);
        return readInt(accessFile);
    }

    private int readNumberOfPalettes() throws IOException {
        accessFile.seek(48);
        return readInt(accessFile);
    }

    private int readLDataOffset() throws IOException {
        accessFile.seek(52);
        return readInt(accessFile);
    }

    private int readLDataLength() throws IOException {
        accessFile.seek(56);
        return readInt(accessFile);
    }

    private int readTDataOffset() throws IOException {
        accessFile.seek(60);
        return readInt(accessFile);
    }

    private int readTDataLength() throws IOException {
        accessFile.seek(64);
        return readInt(accessFile);
    }

    private String readComment() throws IOException {
        accessFile.seek(76);
        byte[] comments = new byte[436];
        accessFile.read(comments);
        return new String(comments).trim();
    }
}
