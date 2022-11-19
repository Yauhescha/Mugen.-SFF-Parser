package com.hescha.parser.sff;

import com.hescha.parser.sff.model.SffFile;
import com.hescha.parser.sff.model.SffHeader;
import com.hescha.parser.wrapper.ReverseByteWrapper;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;


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

SUBFILEHEADER (32 bytes)
-------
Bytes
00-03 File offset where next subfile in the "linked list" is [04]
located. Null if last subfile

04-07 Subfile length (not including header) [04]
Length is 0 if it is a linked sprite
08-09 Image axis X coordinate [02]
10-11 Image axis Y coordinate [02]
12-13 Group number [02]
14-15 Image number (in the group) [02]
16-17 Index of previous copy of sprite (linked sprites only) [02]
This is the actual
18 True if palette is same as previous image [01]
19-31 Blank; can be used for comments [14]
32- PCX graphic data. If palette data is available, it is the last
768 bytes.
\*--------------------------------------------------------------------------*/
public class SffParser {

    public SffFile parse(File file) throws IOException {
        RandomAccessFile accessFile = new RandomAccessFile(file, "r");
        checkSignature(accessFile);

        int version = readVersion(accessFile);
        int numberOfGroup = readNumberOfGroup(accessFile);
        int numberOfImages = readNumberOfImages(accessFile);
        int offsetFirstSubfile = readOffsetFirstSubfile(accessFile);
        int subheaderSite = readSubheaderSite(accessFile);
        int palleteType = readPalleteType(accessFile);
        String comment = readComment(accessFile);


        SffHeader sffHeader = SffHeader.builder()
                .version(version)
                .numberOfGroup(numberOfGroup)
                .numberOfImages(numberOfImages)
                .offsetFirstSubfile(offsetFirstSubfile)
                .subheaderSite(subheaderSite)
                .palleteType(palleteType)
                .comment(comment)
                .build();
        return new SffFile(sffHeader, null);
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

    private int readPalleteType(RandomAccessFile file) throws IOException {
        file.seek(32);
        return file.readByte();
    }

    private String readComment(RandomAccessFile file) throws IOException {
        file.seek(36);
        byte[] comments = new byte[476];
        file.read(comments);
        return new String(comments).trim();
    }



    private static int readInt(RandomAccessFile file) throws IOException {
        byte[] sizeOfSubheaderInBytes = new byte[4];
        file.read(sizeOfSubheaderInBytes);
        return ReverseByteWrapper.getInt(sizeOfSubheaderInBytes);
    }
}
