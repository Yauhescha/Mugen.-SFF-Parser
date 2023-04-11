package com.hescha.parser;

import com.google.common.primitives.Bytes;
import com.hescha.parser.sff.model.v1.SffFile;
import com.hescha.parser.sff.model.v1.SffItem;
import com.hescha.parser.sff.model.v2.Palette;
import com.hescha.parser.sff.model.v2.SffV2File;
import com.hescha.parser.sff.model.v2.SffV2Item;
import com.hescha.parser.sff.parser.v1.SffV1Parser;
import com.hescha.parser.sff.parser.v2.SffV2Parser;
import com.hescha.parser.sff.util.PcxHeaderTemplate;
import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.formats.pcx.PcxImageParser;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Objects;

import static java.lang.Thread.sleep;

public class ShowSpritesVersion2_0_0 {

    private static final PcxImageParser imageParser = new PcxImageParser();

    public static void main(String[] args) throws Exception {
        File files = new File("sff/2.0.0");
        for (File file : Objects.requireNonNull(files.listFiles())) {
            SffV2Parser parser = new SffV2Parser();
            SffV2File parse = parser.parse(file);
            List<SffV2Item> subfiles = parse.getSubfiles();
            List<Palette> palettes = parse.getPalettes();
            showAllImageInOne(subfiles, palettes);
        }
        System.out.println("END");
    }

    private static void showAllImageInOne(List<SffV2Item> subfiles, List<Palette> palettes) {
        JFrame frame = new JFrame("Image");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(500, 500);
        frame.setBounds(20, 20, 500, 500);

        JPanel p = new JPanel();
        ImageIcon icon = new ImageIcon();
        JLabel l = new JLabel(icon);
        l.setBounds(10, 10, 400, 400);
        p.add(l);
        frame.add(p);
        frame.setVisible(true);


        new Thread(() -> {
            while (true) {
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
                        frame.setTitle("Image number: " + i);

                        l.setIcon(new ImageIcon(bufferedImage));
                        Thread.sleep(2000);

                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }).run();
    }
}

