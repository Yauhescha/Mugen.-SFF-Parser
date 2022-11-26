package com.hescha.parser;

import com.hescha.parser.sff.model.v2.SffV2Item;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Shower extends JFrame {
    private final JLabel label = new JLabel(""); //Создаём лейбл


    public Shower(SffV2Item item, BufferedImage bufferedImage) {
        super("Shower image"); //Название программы
        int width = item.getWidth()*10;
        int height = item.getHeight()*10;
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//Что бы она закрывалась
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize(); //Дефолтный набор прав для того что бы узнать width и height
//        this.setLocation(dim.width / 2 - 200, dim.height / 2 - 100); //Позиция проги (левый верхний угол)
        setSize(600, 250); //размер программы
//        JLabel background = new JLabel(new ImageIcon(bufferedImage));
//        add(background);
//        setContentPane(new JLabel(new ImageIcon(bufferedImage)));
        ImageIcon ii = new ImageIcon(bufferedImage);
        ii.getImage();
        setContentPane(new JLabel(new ImageIcon(ii.getImage().getScaledInstance(width, height, Image.SCALE_DEFAULT)))); //Размер рисунка


        label.setBounds(0, 0, width, height); //Размер и позиция лейбла (x и y - координаты width и height ширина и высота)

        Container container = this.getContentPane(); //Добавляем контейнер

        container.add(label);

        this.setVisible(true);
    }
 }
