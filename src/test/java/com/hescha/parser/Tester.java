package com.hescha.parser;

import com.google.common.primitives.Bytes;
import com.hescha.parser.sff.model.v2.Palette;
import com.hescha.parser.sff.model.v2.SffV2File;
import com.hescha.parser.sff.model.v2.SffV2Item;
import com.hescha.parser.sff.parser.v2.SffV2Parser;
import com.hescha.parser.sff.util.PcxHeaderTemplate;
import org.apache.commons.imaging.formats.pcx.PcxImageParser;
import org.apache.commons.io.FileUtils;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.util.List;

public class Tester {
    public static List<SffV2Item> subfiles;

    public static void main(String[] args) throws Exception {
        String filename = "C:\\Users\\Administrator\\Desktop\\mugen\\SM_Hashirama.sff";
//        String filename = "D:\\Download\\MUGEN\\Naruto Konoha Legends M.U.G.E.N\\chars\\SM_Kabuto_Sennin\\SM_Kabuto_Sennin.sff";
        File file = new File(filename);

        SffV2Parser parser = new SffV2Parser();
        SffV2File parse = parser.parse(file);
        List<Palette> palettes = parse.getPalettes();


        PcxImageParser imageParser = new PcxImageParser();
        subfiles = parse.getSubfiles();


        showAllImageInOne(palettes, imageParser);


        System.out.println("END");

    }

    private static void showAllImageInOne(List<Palette> palettes, PcxImageParser imageParser) {
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

    private static void showInOtherTab(List<Palette> palettes, PcxImageParser imageParser) {
        new Thread(() -> {
            try {
                int counter = 0;
                for (int i = 0; i < subfiles.size(); i++) {
                    SffV2Item item = subfiles.get(i);
                    if (item.getCompressionAlgorithm() != 4) continue;
                    System.out.println("image number: " + i);
                    byte[] data = item.getData();
                    byte[] header = PcxHeaderTemplate.getPcxHeaderTemplate(item);
                    byte[] paletteData = palettes.get(item.getPaletteIndex()).getData();
                    byte[] fullData = Bytes.concat(header, data, new byte[]{12}, paletteData);

//                    int skipSprite = 0;
//                    int showSprite = 2;

//                    counter++;
//                    if (counter <= skipSprite) continue;
//                    if (counter > showSprite + skipSprite) break;
//                    System.out.println("try save file " + i);
//                    FileUtils.writeByteArrayToFile(new File("spriteNUmber "+i), data);
//                    FileUtils.writeByteArrayToFile(new File("spriteNUmber "+i+". pallete "), paletteData);

//                    BufferedImage bufferedImage = imageParser.getBufferedImage(fullData, null);
//                    new Shower(item, bufferedImage);

                    InputStream is = new ByteArrayInputStream(fullData);
                    BufferedImage newBi = ImageIO.read(is);
                    new Shower(item, newBi);

                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).run();
    }



}
