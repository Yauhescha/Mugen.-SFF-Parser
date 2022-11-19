package com.hescha.parser.sff.parser;

import com.hescha.parser.sff.model.SffFile;
import com.hescha.parser.sff.model.SffHeader;
import com.hescha.parser.sff.model.SffItem;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class SFFParser {

    public SffFile parse(File file) throws IOException {
        SffHeader header = new SFFHeaderParser().parse(file);
        List<SffItem> subfiles = new SFFSubfileParser().parse(header, file);
        return new SffFile(header, subfiles);
    }
}
