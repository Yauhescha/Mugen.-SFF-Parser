package com.hescha.parser.sff.model.v1;

import com.hescha.parser.sff.model.SffVersion;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SffHeader {
    public static final String signature = "ElecbyteSpr";
    private SffVersion version;
    private int numberOfGroup;
    private int numberOfImages;
    private int offsetFirstSubfile;
    private int subheaderSite;

    /**
     * 1=SPRPALTYPE_SHARED,  0=SPRPALTYPE_INDIV
     */
    private int paletteType;
    private String comment;
}
