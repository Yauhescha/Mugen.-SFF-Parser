package com.hescha.parser;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Shower extends JFrame {
    private final JLabel label = new JLabel(""); //Создаём лейбл


    public Shower(BufferedImage bufferedImage) {
        super("Distantion"); //Название программы
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//Что бы она закрывалась
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize(); //Дефолтный набор прав для того что бы узнать width и height
        this.setLocation(dim.width / 2 - 200, dim.height / 2 - 100); //Позиция проги (левый верхний угол)
        setSize(720, 630); //размер программы
        JLabel background = new JLabel(new ImageIcon(bufferedImage));
        add(background);
        setContentPane(new JLabel(new ImageIcon(bufferedImage)));
        ImageIcon ii = new ImageIcon(bufferedImage);
        ii.getImage();
        setContentPane(new JLabel(new ImageIcon(ii.getImage().getScaledInstance(720, 630, Image.SCALE_DEFAULT)))); //Размер рисунка


        label.setBounds(30, 25, 720, 360); //Размер и позиция лейбла (x и y - координаты width и height ширина и высота)

        Container container = this.getContentPane(); //Добавляем контейнер

        container.add(label);

        this.setVisible(true);
    }
 }
