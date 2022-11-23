package com.hescha.parser.sff.model.v1;

import com.google.common.primitives.Bytes;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SffItem {
    private int nextSubfileOffset;
    private int subfileLength;
    private int imageAxisX;
    private int imageAxisY;
    private int groupNumber;
    private int imageNumber;
    private int previousCopySprite;
    private boolean paletteAsPrevious;
    private String comment;
    private byte[] pcxGraphicData;
    private byte[] pcxPalette;

    // TODO: update this method or create a new one
    public byte[] getImageGraphicData() {
        if (paletteAsPrevious) {
            return Bytes.concat(pcxGraphicData, pcxPalette);
        } else {
            return pcxGraphicData;
        }
    }
}
