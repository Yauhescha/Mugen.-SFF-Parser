package com.hescha.parser.sff.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SffHeader {
    public static final String signature = "ElecbyteSpr";
    private int version;
    private int numberOfGroup;
    private int numberOfImages;
    private int offsetFirstSubfile;
    private int subheaderSite;
    // (1=SPRPALTYPE_SHARED or 0=SPRPALTYPE_INDIV)
    private int paletteType;
    private String comment;
}
