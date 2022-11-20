package com.hescha.parser.sff.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SffVersion {
    private byte verhi;
    private byte verlo1;
    private byte verlo2;
    private byte verlo3;

    public static boolean isFileVersion1Dot1(SffVersion version) {
        return version.verhi == 0 && version.verlo1 == 1 && version.verlo2 == 0 && version.verlo3 == 1;
    }

    public static boolean isFileVersion2Dot0(SffVersion version) {
        return version.verhi == 0 && version.verlo1 == 0 && version.verlo2 == 0 && version.verlo3 == 2;
    }

    public static boolean isFileVersion2Dot1(SffVersion version) {
        return version.verhi == 0 && version.verlo1 == 1 && version.verlo2 == 0 && version.verlo3 == 2;
    }

}
