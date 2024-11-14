package org.example;

import GUI.MainGUI;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainGUI::new);
    }
}