package com.hescha.parser;

import com.hescha.parser.sff.model.v1.SffFile;
import com.hescha.parser.sff.model.v1.SffItem;
import com.hescha.parser.sff.parser.v1.SffV1Parser;
import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.formats.pcx.PcxImageParser;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

import static java.lang.Thread.sleep;

public class ShowSpritesVersion1_0_1 {
    public static void main(String[] args) throws Exception {
        File files = new File("sff/1.0.1");
        for (File file : Objects.requireNonNull(files.listFiles())) {
            SffV1Parser parser = new SffV1Parser();
            SffFile parse = parser.parse(file);
            List<SffItem> subfiles = parse.getSubfiles();
            PcxImageParser imageParser = new PcxImageParser();
            showAllImageInOne(subfiles, imageParser);
        }
    }

    private static void showAllImageInOne(List<SffItem> subfiles, PcxImageParser imageParser) {
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


        runThread(subfiles, imageParser, frame, l);
    }

    private static void runThread(List<SffItem> subfiles, PcxImageParser imageParser, JFrame frame, JLabel l) {
        new Thread(() -> {
            while (true) {
                for (int i = 0; i < subfiles.size(); i++) {
                    SffItem item = subfiles.get(i);
                    System.out.println("image number: " + i);
                    byte[] imageGraphicData = item.getImageGraphicData();
                    try {
                        BufferedImage bufferedImage = imageParser.getBufferedImage(imageGraphicData, null);
                        frame.setTitle("Image number: " + i);

                        l.setIcon(new ImageIcon(bufferedImage));
                        sleep(100);

                    } catch (IOException | InterruptedException | ImageReadException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }).start();
    }
}
