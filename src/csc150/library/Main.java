package csc150.library;

import csc150.library.controllers.HTTPController;
import csc150.library.controllers.MainController;
import csc150.library.views.LibraryUI;
import javax.swing.*;

public class Main {
    public static void main(String[] args) {
      new MainController().run();
    }
}