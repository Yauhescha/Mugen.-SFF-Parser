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

}
