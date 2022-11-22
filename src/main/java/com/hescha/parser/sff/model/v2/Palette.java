package com.hescha.parser.sff.model.v2;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Palette {
    private int groupNumber;
    private int itemNumber;
    private int columNumber;
    private int linkedPalette;
    private int offsetIntoData;
    private int dataLength;
    private byte[] data;

}
