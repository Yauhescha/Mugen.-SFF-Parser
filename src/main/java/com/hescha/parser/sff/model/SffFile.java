package com.hescha.parser.sff.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SffFile {
    private SffHeader header;
    private List<SffItem> subfiles = new ArrayList<>();
}
