package com.hescha.parser;

import com.google.common.primitives.Bytes;
import com.hescha.parser.sff.model.v2.Palette;
import com.hescha.parser.sff.model.v2.SffV2File;
import com.hescha.parser.sff.model.v2.SffV2Item;
import com.hescha.parser.sff.parser.v2.SffV2Parser;
import com.hescha.parser.sff.util.PcxHeaderTemplate;
import org.apache.commons.imaging.formats.pcx.PcxImageParser;
import org.apache.commons.io.FileUtils;

import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;

public class Tester {
    public static List<SffV2Item> subfiles;

    public static void main(String[] args) throws Exception {
        String filename = "C:\\Users\\Administrator\\Desktop\\mugen\\SM_Hashirama.sff";
        File file = new File(filename);

        SffV2Parser parser = new SffV2Parser();
        SffV2File parse = parser.parse(file);
        List<Palette> palettes = parse.getPalettes();


        PcxImageParser imageParser = new PcxImageParser();
        subfiles = parse.getSubfiles();

        new Thread(() -> {
                for (int i = 0; i < subfiles.size(); i++) {
                    SffV2Item item = subfiles.get(i);
                    if (item.getCompressionAlgorithm() != 4) continue;
                    System.out.println("image number: " + i);
                    byte[] data = item.getData();
                    byte[] header = PcxHeaderTemplate.getPcxHeaderTemplate(item);
                    byte[] paletteData = palettes.get(item.getPaletteIndex()).getData();

                    byte[] fullData = Bytes.concat(header, data, new byte[]{12}, paletteData);

                    try {
                        BufferedImage bufferedImage = imageParser.getBufferedImage(fullData, null);
                        new Shower(bufferedImage);
//                        break;
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
//            }
        }).run();


        System.out.println("END");

    }


}
