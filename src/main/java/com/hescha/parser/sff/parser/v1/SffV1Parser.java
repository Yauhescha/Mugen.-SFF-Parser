package com.hescha.parser.sff.parser.v1;

import com.hescha.parser.sff.model.v1.SffFile;
import com.hescha.parser.sff.model.v1.SffHeader;
import com.hescha.parser.sff.model.v1.SffItem;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * The SffV1Parser program implements an application that
 * parse SFFv1 file and read all information include all sprites.
 * You can display your pcx sprite via PcxImageParser.
 *
 * @author  Yauhescha
 * @version 1.0
 * @since   2022-13-22
 */
public class SffV1Parser {

    public SffFile parse(File file) throws IOException {
        SffHeader header = new SffV1HeaderParser().parse(file);
        List<SffItem> subfiles = new SffV1SubfileParser().parse(header, file);
        return new SffFile(header, subfiles);
    }
}
