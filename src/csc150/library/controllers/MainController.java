/**
 * @author rshirts
 * @createdOn 2/27/2023 at 6:42 PM
 * @projectName LibraryV4
 * @packageName csc150.library.controllers;
 */
package csc150.library.controllers;

import csc150.library.views.LibraryUI;

import javax.swing.*;

public class MainController {
    LibraryUI ui = new LibraryUI();
    FileController file = new FileController();

    public void run() {
        ui.MyGUI();
        ui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ui.setVisible(true);
    }
}
