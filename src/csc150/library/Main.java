package csc150.library;

import csc150.library.controllers.HTTPController;
import csc150.library.views.LibraryUI;
import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        new HTTPController().run();
        LibraryUI ui = new LibraryUI();
        ui.MyGUI();
        ui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ui.setVisible(true);
    }
}