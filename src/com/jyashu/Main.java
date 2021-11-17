package com.jyashu;

import javax.swing.JFrame;
import java.awt.*;

/**
 *
 * @author J Yashu
 */


public class Main {

    public static void main(String[] args) {
        JFrame frame=new JFrame();
        Gameplay gamePlay = new Gameplay();

        frame.setBounds(10, 10, 700, 650);
        Image icon = Toolkit.getDefaultToolkit().getImage("res/icon/deflecto1.png");
        frame.setIconImage(icon);
        frame.setTitle("Deflecto");
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(gamePlay);
        frame.setVisible(true);

    }

}
