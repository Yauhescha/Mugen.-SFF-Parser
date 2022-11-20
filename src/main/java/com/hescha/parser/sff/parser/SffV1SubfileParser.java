package com.hescha.parser.sff.parser;

import com.hescha.parser.sff.model.SffHeader;
import com.hescha.parser.sff.model.SffItem;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.hescha.parser.wrapper.ReverseByteWrapper.readInt;
import static com.hescha.parser.wrapper.ReverseByteWrapper.readShort;

/*
|--------------------------------------------------*\
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
public class SffV1SubfileParser {
    private int subfileOffsetStartpoint = 512;

    public List<SffItem> parse(SffHeader header, File file) throws IOException {
        List<SffItem> list = new ArrayList<>();
        SffItem sffItem;
        RandomAccessFile accessFile = new RandomAccessFile(file, "r");

        subfileOffsetStartpoint = header.getOffsetFirstSubfile();
        do {
            sffItem = parseSubfile(accessFile);
            correctSprite(list, sffItem);
            list.add(sffItem);

            subfileOffsetStartpoint = sffItem.getNextSubfileOffset();
            accessFile.seek(subfileOffsetStartpoint);
        } while (accessFile.length() != subfileOffsetStartpoint);

        accessFile.close();
        return list;
    }

    private void correctSprite(List<SffItem> list, SffItem sffItem) {
        if (sffItem.getPreviousCopySprite() != 0) {
            loadCopySprite(list, sffItem);
        } else {
            correctPalette(sffItem, list);
        }
    }

    private void loadCopySprite(List<SffItem> list, SffItem sffItem) {
        SffItem previous = list.get(sffItem.getPreviousCopySprite());
        sffItem.setPcxGraphicData(previous.getPcxGraphicData());
        sffItem.setPcxPalette(previous.getPcxPalette());
        sffItem.setPaletteAsPrevious(previous.isPaletteAsPrevious());
    }

    private void correctPalette(SffItem sffItem, List<SffItem> list) {
        if (sffItem.isPaletteAsPrevious()) {
            SffItem last = list.get(list.size() - 1);
            sffItem.setPcxPalette(last.getPcxPalette());
        } else {
            byte[] graphicData = sffItem.getPcxGraphicData();
            byte[] palette = Arrays.copyOfRange(graphicData, graphicData.length - 768, graphicData.length);
            sffItem.setPcxPalette(palette);
        }
    }

    private SffItem parseSubfile(RandomAccessFile accessFile) throws IOException {
        int nextSubfileOffset = readNextSubfileOffset(accessFile);
        int subfileLength = readSubfileLength(accessFile);
        int imageAxisX = readImageAxisX(accessFile);
        int imageAxisY = readImageAxisY(accessFile);
        int groupNumber = readGroupNumber(accessFile);
        int imageNumber = readImageNumber(accessFile);
        int previousCopySprite = readPreviousCopySprite(accessFile);
        boolean paletteAsPrevious = readPaletteAsPrevious(accessFile);
        String comment = readComment(accessFile);
        byte[] pcxGraphicData = readPcxGraphicData(accessFile, subfileLength);

        return SffItem.builder()
                .nextSubfileOffset(nextSubfileOffset)
                .subfileLength(subfileLength)
                .imageAxisX(imageAxisX)
                .imageAxisY(imageAxisY)
                .groupNumber(groupNumber)
                .imageNumber(imageNumber)
                .previousCopySprite(previousCopySprite)
                .paletteAsPrevious(paletteAsPrevious)
                .comment(comment)
                .pcxGraphicData(pcxGraphicData)
                .build();
    }

    private int readNextSubfileOffset(RandomAccessFile file) throws IOException {
        file.seek(subfileOffsetStartpoint);
        return readInt(file);
    }

    private int readSubfileLength(RandomAccessFile file) throws IOException {
        file.seek(subfileOffsetStartpoint + 4);
        return readInt(file);
    }

    private int readImageAxisX(RandomAccessFile file) throws IOException {
        file.seek(subfileOffsetStartpoint + 8);
        return readShort(file);
    }

    private int readImageAxisY(RandomAccessFile file) throws IOException {
        file.seek(subfileOffsetStartpoint + 10);
        return readShort(file);
    }

    private int readGroupNumber(RandomAccessFile file) throws IOException {
        file.seek(subfileOffsetStartpoint + 12);
        return readShort(file);
    }

    private int readImageNumber(RandomAccessFile file) throws IOException {
        file.seek(subfileOffsetStartpoint + 14);
        return readShort(file);
    }

    private int readPreviousCopySprite(RandomAccessFile file) throws IOException {
        file.seek(subfileOffsetStartpoint + 16);
        return readShort(file);
    }

    private boolean readPaletteAsPrevious(RandomAccessFile file) throws IOException {
        file.seek(subfileOffsetStartpoint + 18);
        return file.readBoolean();
    }

    private String readComment(RandomAccessFile file) throws IOException {
        file.seek(subfileOffsetStartpoint + 19);
        byte[] comments = new byte[14];
        file.read(comments);
        return new String(comments).trim();
    }

    private byte[] readPcxGraphicData(RandomAccessFile file, int subfileLength) throws IOException {
        file.seek(subfileOffsetStartpoint + 32);
        byte[] pcxData = new byte[subfileLength];
        file.read(pcxData);

        return pcxData;
    }
}
