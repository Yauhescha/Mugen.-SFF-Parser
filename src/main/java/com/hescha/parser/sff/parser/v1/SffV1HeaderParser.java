package com.hescha.parser.sff.parser.v1;

import com.hescha.parser.sff.exception.VersionDoesNotSupportedException;
import com.hescha.parser.sff.model.v1.SffHeader;
import com.hescha.parser.sff.model.SffVersion;
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
public class SffV1HeaderParser extends SffHeaderParser {

    private RandomAccessFile accessFile;

    public SffHeader parse(File file) throws IOException {
        accessFile = new RandomAccessFile(file, "r");
        checkSignature(accessFile);

        SffVersion version = readVersion();
        int numberOfGroup = readNumberOfGroup();
        int numberOfImages = readNumberOfSprites();
        int offsetFirstSubfile = readOffsetFirstSpriteNode();
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

    private SffVersion readVersion() throws IOException {
        accessFile.seek(12);
        byte verhi = accessFile.readByte();
        byte verlo1 = accessFile.readByte();
        byte verlo2 = accessFile.readByte();
        byte verlo3 = accessFile.readByte();
        SffVersion sffVersion = new SffVersion(verhi, verlo1, verlo2, verlo3);
        if (!SffVersion.isFileVersion1Dot1(sffVersion)) {
            throw new VersionDoesNotSupportedException(sffVersion);
        }
        return sffVersion;
    }

    private int readNumberOfGroup() throws IOException {
        accessFile.seek(16);
        return readInt(accessFile);
    }

    private int readNumberOfSprites() throws IOException {
        accessFile.seek(20);
        return readInt(accessFile);
    }

    private int readOffsetFirstSpriteNode() throws IOException {
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
