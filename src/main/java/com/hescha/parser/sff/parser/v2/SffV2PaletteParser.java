package com.hescha.parser.sff.parser.v2;

import com.hescha.parser.sff.model.v2.Palette;
import com.hescha.parser.sff.model.v2.SffV2Header;
import com.hescha.parser.sff.parser.SffHeaderParser;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

import static com.hescha.parser.wrapper.ReverseByteWrapper.readInt;
import static com.hescha.parser.wrapper.ReverseByteWrapper.readShort;

/*--| SFF file structure
|--------------------------------------------------*\
Pal node header 2.00 16 bytes

dec  hex  size   meaning
0    0     2  groupno
2    2     2  itemno
4    4     2  numcols
6    6     2  Index number of the linked palette (if linked)
8    8     4  Offset into ldata
12    C     4  Palette data length (0: linked)
Palette data is stored in 4 byte chunks per color. The first 3 bytes correspond to 8-bit values for RGB color, and the last byte is unused (set to 0).
\*--------------------------------------------------------------------------*/
public class SffV2PaletteParser extends SffHeaderParser {

    private static final byte PALETTE_SIZE_IN_BYTES = 16;
    private RandomAccessFile accessFile;
    int firstOffset;

    public List<Palette> parse(SffV2Header header, File file) throws IOException {
        accessFile = new RandomAccessFile(file, "r");
        List<Palette> palettes = new ArrayList<>();

        firstOffset = header.getOffsetFirstPaletteNode();
        for (int paletteNumber = 0; paletteNumber < header.getNumberOfPalettes(); paletteNumber++) {
            palettes.add(parsePaletteByNumber(paletteNumber));
        }

        return palettes;
    }

    private Palette parsePaletteByNumber(int paletteNumber) throws IOException {
        accessFile.seek(firstOffset + paletteNumber * PALETTE_SIZE_IN_BYTES);
        int groupNumber = readShort(accessFile);
        int itemNumber = readShort(accessFile);
        int columNumber = readShort(accessFile);
        int linkedPalette = readShort(accessFile);
        int offsetIntoData = readInt(accessFile);
        int dataLength = readInt(accessFile);

        return Palette.builder()
                .groupNumber(groupNumber)
                .itemNumber(itemNumber)
                .columNumber(columNumber)
                .linkedPalette(linkedPalette)
                .offsetIntoData(offsetIntoData)
                .dataLength(dataLength)
                .build();
    }
}