package com.hescha.parser.sff.parser;

import com.hescha.parser.sff.model.SffHeader;
import com.hescha.parser.wrapper.ReverseByteWrapper;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;

import static com.hescha.parser.wrapper.ReverseByteWrapper.readInt;

/*--| SFF file structure
|--------------------------------------------------*\
Version 1.01
HEADER (512 bytes)
------
Bytes
00-11 "ElecbyteSpr\0" signature [12]
12-15 1 verhi, 1 verlo, 1 verlo2, 1 verlo3 [04]
16-19 Number of groups [04]
20-24 Number of images [04]
24-27 File offset where first subfile is located [04]
28-31 Size of subheader in bytes [04]
32 Palette type (1=SPRPALTYPE_SHARED or 0=SPRPALTYPE_INDIV) [01]
33-35 Blank; set to zero [03]
36-511 Blank; can be used for comments [476]
\*--------------------------------------------------------------------------*/
public class SFFHeaderParser {
    public SffHeader parse(File file) throws IOException {

        RandomAccessFile accessFile = new RandomAccessFile(file, "r");
        checkSignature(accessFile);

        int version = readVersion(accessFile);
        int numberOfGroup = readNumberOfGroup(accessFile);
        int numberOfImages = readNumberOfImages(accessFile);
        int offsetFirstSubfile = readOffsetFirstSubfile(accessFile);
        int subheaderSite = readSubheaderSite(accessFile);
        int paletteType = readPaletteType(accessFile);
        String comment = readComment(accessFile);

        accessFile.close();

        return SffHeader.builder()
                .version(version)
                .numberOfGroup(numberOfGroup)
                .numberOfImages(numberOfImages)
                .offsetFirstSubfile(offsetFirstSubfile)
                .subheaderSite(subheaderSite)
                .paletteType(paletteType)
                .comment(comment)
                .build();
    }

    private void checkSignature(RandomAccessFile file) throws IOException {
        file.seek(0);
        byte[] signature = new byte[11];
        file.read(signature);
        if (!SffHeader.signature.equals(new String(signature))) {
            throw new RuntimeException("This is not ssf file.");
        }
    }

    private int readVersion(RandomAccessFile file) throws IOException {
        file.seek(12);
        byte[] version = new byte[4];
        file.read(version);
        return ByteBuffer.wrap(version).getShort();
    }

    private int readNumberOfGroup(RandomAccessFile file) throws IOException {
        file.seek(16);
        return readInt(file);
    }

    private int readNumberOfImages(RandomAccessFile file) throws IOException {
        file.seek(20);
        return readInt(file);
    }

    private int readOffsetFirstSubfile(RandomAccessFile file) throws IOException {
        file.seek(24);
        return readInt(file);
    }

    private int readSubheaderSite(RandomAccessFile file) throws IOException {
        file.seek(28);
        return readInt(file);
    }

    private int readPaletteType(RandomAccessFile file) throws IOException {
        file.seek(32);
        return file.readByte();
    }

    private String readComment(RandomAccessFile file) throws IOException {
        file.seek(36);
        byte[] comments = new byte[476];
        file.read(comments);
        return new String(comments).trim();
    }
}
