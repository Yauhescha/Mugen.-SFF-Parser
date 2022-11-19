package com.hescha.parser.sff;

import com.hescha.parser.sff.model.SffFile;
import com.hescha.parser.sff.model.SffItem;
import com.hescha.parser.sff.parser.SFFParser;
import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.formats.pcx.PcxImageParser;

import javax.imageio.ImageReader;
import javax.imageio.spi.ImageReaderSpi;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class Main {

    public static void main(String[] args) throws Exception {
        System.out.println("START");

        String filename1 = "AM_Kisame.sff"; // 1715 sprites, 135 groups
//        String filename2 = "Danzo.sff"; // 921 sprites, 110 groups
//        String filename3 = "stage.sff"; // 5 sprites, 4 groups
//        String filename4 = "one.sff"; // 1 sprites, 1 groups

        SFFParser parser = new SFFParser();
        SffFile parse1 = parser.parse(new File(filename1));
//        SffFile parse2 = parser.parse(new File(filename2));
//        SffFile parse3 = parser.parse(new File(filename3));
//        SffFile parse4 = parser.parse(new File(filename4));

        PcxImageParser imageParser = new PcxImageParser();
        List<SffItem> subfiles = parse1.getSubfiles();


        JFrame f = new JFrame("Image");
        f.setSize(500, 500);
        f.setBounds(20, 20, 500, 500);

        JPanel p = new JPanel();
        ImageIcon icon = new ImageIcon();
        JLabel l = new JLabel(icon);
        l.setBounds(10, 10, 400, 400);
        p.add(l);
        f.add(p);
        f.setVisible(true);


        new Thread(() -> {
            while (true) {
                for (int i = 0; i < subfiles.size(); i++) {
                    byte[] pcxGraphicData = subfiles.get(i).getFullGraphicData();
                    if(subfiles.get(i).getPcxPalette()==null)continue;
                    try {
                        BufferedImage bufferedImage = imageParser.getBufferedImage(pcxGraphicData, null);

                        l.setIcon(new ImageIcon(bufferedImage));
                        Thread.sleep(100);

                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }).run();


        System.out.println("END");

    }

}


