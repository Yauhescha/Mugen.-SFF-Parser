package com.hescha.parser.sff.v1;

import com.hescha.parser.sff.model.v1.SffFile;
import com.hescha.parser.sff.model.v1.SffItem;
import com.hescha.parser.sff.parser.v1.SffV1Parser;
import org.apache.commons.imaging.formats.pcx.PcxImageParser;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;

public class ShowAllSprites {

    public static void main(String[] args) throws Exception {
        System.out.println("START");

        String filename = "src\\test\\resources\\v1.sff";

        SffV1Parser parser = new SffV1Parser();
        SffFile parse = parser.parse(new File(filename));

        PcxImageParser imageParser = new PcxImageParser();
        List<SffItem> subfiles = parse.getSubfiles();


        JFrame f = new JFrame("Image");
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
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
                    System.out.println(i);
                    byte[] pcxGraphicData = subfiles.get(i).getImageGraphicData();
                    if (subfiles.get(i).getPcxPalette() == null) continue;
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


