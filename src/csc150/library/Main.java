package csc150.library;

import csc150.library.controllers.LibraryClient;
import csc150.library.controllers.MainController;
import csc150.library.models.KeyPossibilities;

import java.util.List;

public class Main {
    public static void main(String[] args) {
      new MainController().run();

      //region testing
        System.out.println(new LibraryClient().getBookByISBN(List.of(KeyPossibilities.TITLE), "9781473223035"));
        //endregion

    }
}