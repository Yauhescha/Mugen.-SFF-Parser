package com.hescha.parser.sff.model.v2;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SffV2File {
    private SffV2Header header;
    private List<SffV2Item> subfiles = new ArrayList<>();

    private List<Palette> palettes = new ArrayList<>();
}
