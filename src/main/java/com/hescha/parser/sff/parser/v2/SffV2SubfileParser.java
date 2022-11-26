package com.hescha.parser.sff.parser.v2;

import com.hescha.parser.sff.model.v2.SffV2Header;
import com.hescha.parser.sff.model.v2.SffV2Item;
import com.hescha.parser.sff.parser.v2.decoder.Decoder;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

import static com.hescha.parser.sff.util.ReverseByteWrapper.readInt;
import static com.hescha.parser.sff.util.ReverseByteWrapper.readShort;

/*
|--------------------------------------------------*\
Sprite node header 2.00 28 bytes

dec  hex  size   meaning
0    0     2   groupno
2    2     2   itemno
4    4     2   width
6    6     2   height
8    8     2   axisx
10    A     2   axisy
12    C     2   Index number of the linked sprite (if linked)
14    E     1   fmt
15    F     1   coldepth
16   10     4   offset into ldata or tdata
20   14     4   Sprite data length (0: linked)
24   18     2   palette index
26   1A     2   flags
\*--------------------------------------------------------------------------*/
public class SffV2SubfileParser {

    private static final byte SPRITE_SIZE_IN_BYTES = 28;
    private RandomAccessFile accessFile;
    int firstOffset;
    int generalOffset;

    public List<SffV2Item> parse(SffV2Header header, File file) throws IOException {
        List<SffV2Item> sprites = new ArrayList<>();
        this.accessFile = new RandomAccessFile(file, "r");

        firstOffset = header.getOffsetFirstSpriteNode();
        for (int spriteNumber = 0; spriteNumber < header.getNumberOfSprites(); spriteNumber++) {
            SffV2Item sprite = parseSpriteByNumber(spriteNumber);
            updateSpriteData(header, sprites, sprite);
            sprites.add(sprite);
        }

        return sprites;
    }

    private SffV2Item parseSpriteByNumber(int spriteNumber) throws IOException {
        accessFile.seek(firstOffset + (long) spriteNumber * SPRITE_SIZE_IN_BYTES);

        int groupNumber = readShort(accessFile);
        int imageNumber = readShort(accessFile);
        int width = readShort(accessFile);
        int height = readShort(accessFile);
        int imageAxisX = readShort(accessFile);
        int imageAxisY = readShort(accessFile);
        int linkedSpriteNumber = readShort(accessFile);
        int compressionAlgorithm = accessFile.readByte();
        int biDepth = accessFile.readByte();
        int offset = readInt(accessFile);
        int dataLength = readInt(accessFile);
        int paletteIndex = readShort(accessFile);
        int flags = readShort(accessFile);

        return SffV2Item.builder()
                .groupNumber(groupNumber)
                .imageNumber(imageNumber)
                .width(width)
                .height(height)
                .imageAxisX(imageAxisX)
                .imageAxisY(imageAxisY)
                .linkedSpriteNumber(linkedSpriteNumber)
                .compressionAlgorithm(compressionAlgorithm)
                .biDepth(biDepth)
                .offset(offset)
                .dataLength(dataLength)
                .paletteIndex(paletteIndex)
                .flags(flags)
                .build();
    }

    // TODO: use link to sprite instead of copying
    private void updateSpriteData(SffV2Header header, List<SffV2Item> sprites, SffV2Item sprite) throws IOException {
        generalOffset = sprite.getFlags() == 0 ? header.getLDataOffset() : header.getTDataOffset();
        if (sprite.getLinkedSpriteNumber() == 0) {
            accessFile.seek(generalOffset + sprite.getOffset());
            byte[] data = new byte[sprite.getDataLength()];
            accessFile.read(data);

            byte[] decodedData = Decoder.decode(sprite, data);
            sprite.setData(decodedData);
        } else {
            sprite.setData(sprites.get(sprite.getLinkedSpriteNumber()).getData());
        }

    }
}
