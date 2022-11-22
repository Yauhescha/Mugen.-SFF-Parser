package com.hescha.parser;

import com.google.common.primitives.Bytes;
import com.hescha.parser.sff.model.v2.Palette;
import com.hescha.parser.sff.model.v2.SffV2File;
import com.hescha.parser.sff.model.v2.SffV2Item;
import com.hescha.parser.sff.parser.v2.SffV2Parser;
import com.hescha.parser.wrapper.PcxHeaderTemplate;
import org.apache.commons.imaging.formats.pcx.PcxImageParser;

import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;

public class Tester {
    public static List<SffV2Item> subfiles;
    public static void main(String[] args) throws Exception {
//        String filename = "C:\\Users\\Administrator\\Desktop\\mugen\\2.1png.sff";
//        String filename = "C:\\Users\\Administrator\\Desktop\\mugen\\2.0.sff";
        String filename = "D:\\Download\\MUGEN\\Naruto Konoha Legends M.U.G.E.N\\chars\\AM_Kisame - Copy\\AM_Kisame.sff";
        File file = new File(filename);

        SffV2Parser parser = new SffV2Parser();
        SffV2File parse = parser.parse(file);
//        SffV2Item v2Item = parse.getSubfiles().get(0);
        List<Palette> palettes = parse.getPalettes();

//        byte[] data = v2Item.getData();
//        byte[] decodedData = fromRLE8(data);
//        byte[] header = PcxHeaderTemplate.getPcxHeaderTemplate(v2Item);
//        byte[] paletteData = palettes.get(v2Item.getPaletteIndex()).getData();
//
//        byte[] fullData = Bytes.concat(header, decodedData);

//        long newFilename = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
//        FileUtils.writeByteArrayToFile(new File("C:\\Users\\Administrator\\Desktop\\mugen\\"
//                + newFilename+".pcx"), fullData);
//
//        System.out.println("newFilename: " + newFilename);

        PcxImageParser imageParser = new PcxImageParser();
        subfiles = parse.getSubfiles();


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
                    System.out.println("image number: " + i);
                    SffV2Item item = subfiles.get(i);
                    byte[] data = item.getData();
                    if(data.length==0 )continue;
//                    byte[] decodedData = fromRLE8(data);
                    byte[] header = PcxHeaderTemplate.getPcxHeaderTemplate(item);
                    byte[] paletteData = palettes.get(item.getPaletteIndex()).getData();

                    byte[] fullData = Bytes.concat(header, data, new byte[]{12}, paletteData);

                    try {
                        BufferedImage bufferedImage = imageParser.getBufferedImage(fullData, null);

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
