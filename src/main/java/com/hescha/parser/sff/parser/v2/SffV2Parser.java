package com.hescha.parser.sff.parser.v2;

import com.hescha.parser.sff.model.v2.Palette;
import com.hescha.parser.sff.model.v2.SffV2File;
import com.hescha.parser.sff.model.v2.SffV2Header;
import com.hescha.parser.sff.model.v2.SffV2Item;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * The SffV2Parser program implements an application that
 * parse SFFv2 file and read all information include all sprites.
 * You should know, that this parser only READ information.
 * REMEMBER: Sprite data does not decomposed!
 *
 * @author  Yauhescha
 * @version 1.0
 * @since   2022-13-22
 */
public class SffV2Parser {

    public SffV2File parse(File file) throws IOException {
        SffV2Header header = new SffV2HeaderParser().parse(file);
        List<Palette> palettes = new SffV2PaletteParser().parse(header, file);
        List<SffV2Item> subfiles = new SffV2SubfileParser().parse(header, file);
        return new SffV2File(header, subfiles, palettes);
    }
}
