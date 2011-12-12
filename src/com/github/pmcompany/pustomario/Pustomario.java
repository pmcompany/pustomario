package com.github.pmcompany.pustomario;

import javax.swing.*;

/**
 * @author dector (dector9@gmail.com)
 */
public class Pustomario {
    public static void main(String[] args) {

        String gameName;
        gameName = JOptionPane.showInputDialog("Enter player's name");

        Client c = new Client(gameName);
        c.run();

        System.out.println("See you next time!");
    }
}
