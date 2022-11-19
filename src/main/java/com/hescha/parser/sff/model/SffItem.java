package com.hescha.parser.sff.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SffItem {
    private Integer nextSubfileOffset;
    private int subfileLength; //without header
    private int imageAxisX;
    private int imageAxisY;
    private int groupNumber;
    private int imageNumber;
    private Integer previousCopySprite;
    private boolean paletteAsPrevious;
    private String comment;
    private byte[] pcxGraphicData;
}
