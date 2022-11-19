package com.hescha.parser.sff;

import com.hescha.parser.sff.model.SffFile;
import com.hescha.parser.sff.parser.SFFParser;
import org.apache.commons.imaging.formats.pcx.PcxImageParser;

import javax.imageio.ImageReader;
import javax.imageio.spi.ImageReaderSpi;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class Main {

    public static void main(String[] args) throws Exception {
        System.out.println("START");

//        String filename1 = "AM_Kisame.sff"; // 1715 sprites, 135 groups
//        String filename2 = "Danzo.sff"; // 921 sprites, 110 groups
//        String filename3 = "stage.sff"; // 5 sprites, 4 groups
        String filename4 = "one.sff"; // 1 sprites, 1 groups

        SFFParser parser = new SFFParser();
//        SffFile parse1 = parser.parse(new File(filename1));
//        SffFile parse2 = parser.parse(new File(filename2));
//        SffFile parse3 = parser.parse(new File(filename3));
        SffFile parse4 = parser.parse(new File(filename4));

        PcxImageParser imageParser = new PcxImageParser();
        byte[] pcxGraphicData = parse4.getSubfiles().get(0).getPcxGraphicData();
        BufferedImage bufferedImage = imageParser.getBufferedImage(pcxGraphicData, null);


        JFrame f = new JFrame("Image");
        f.setSize(500, 500);
        f.setBounds(20, 20, 500, 500);

        JPanel p = new JPanel();
        ImageIcon icon = new ImageIcon(bufferedImage);
        JLabel l = new JLabel(icon);
        l.setBounds(10, 10, 400, 400);
        p.add(l);
        f.add(p);
        f.setVisible(true);


        System.out.println("END");

    }

}


