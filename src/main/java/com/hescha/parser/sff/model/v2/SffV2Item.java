package com.hescha.parser.sff.model.v2;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SffV2Item {
    private int groupNumber;
    private int imageNumber;
    private int width;
    private int height;
    private int imageAxisX;
    private int imageAxisY;
    private int linkedSpriteNumber;
    private int compressionAlgorithm;
    private int biDepth;
    private int offset;
    private int dataLength;
    private int paletteIndex;
    private int flags;
}
