package com.hescha.parser.sff.model.v2;

import com.hescha.parser.sff.model.SffVersion;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SffV2Header {
    public static final String signature = "ElecbyteSpr";
    private SffVersion version;
    private SffVersion compatVersion;
    private int offsetFirstSpriteNode;
    private int numberOfSprites;
    private int offsetFirstPaletteNode;
    private int numberOfPalettes;
    private int lDataOffset;
    private int lDataLength;
    private int tDataOffset;
    private int tDataLength;
    private String comment;
}
