package com.hescha.parser.sff.parser;

import com.hescha.parser.sff.exception.SignatureException;
import com.hescha.parser.sff.exception.VersionDoesNotSupportedException;
import com.hescha.parser.sff.model.SffHeader;

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
public class SffV1HeaderParser {

    private RandomAccessFile accessFile;

    public SffHeader parse(File file) throws IOException {
        accessFile = new RandomAccessFile(file, "r");
        checkSignature();

        int version = readVersion();
        int numberOfGroup = readNumberOfGroup();
        int numberOfImages = readNumberOfImages();
        int offsetFirstSubfile = readOffsetFirstSubfile();
        int subheaderSite = readSubheaderSite();
        int paletteType = readPaletteType();
        String comment = readComment();

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

    private void checkSignature() throws IOException {
        accessFile.seek(0);
        byte[] signature = new byte[11];
        accessFile.read(signature);
        if (!SffHeader.signature.equals(new String(signature))) {
            throw new SignatureException();
        }
    }

    private int readVersion() throws IOException {
        accessFile.seek(12);
        byte[] version = new byte[4];
        accessFile.read(version);
        short aShort = ByteBuffer.wrap(version).getShort();
        if(aShort!=1){
            throw new VersionDoesNotSupportedException();
        }
        return aShort;
    }

    private int readNumberOfGroup() throws IOException {
        accessFile.seek(16);
        return readInt(accessFile);
    }

    private int readNumberOfImages() throws IOException {
        accessFile.seek(20);
        return readInt(accessFile);
    }

    private int readOffsetFirstSubfile() throws IOException {
        accessFile.seek(24);
        return readInt(accessFile);
    }

    private int readSubheaderSite() throws IOException {
        accessFile.seek(28);
        return readInt(accessFile);
    }

    private int readPaletteType() throws IOException {
        accessFile.seek(32);
        return accessFile.readByte();
    }

    private String readComment() throws IOException {
        accessFile.seek(36);
        byte[] comments = new byte[476];
        accessFile.read(comments);
        return new String(comments).trim();
    }
}
