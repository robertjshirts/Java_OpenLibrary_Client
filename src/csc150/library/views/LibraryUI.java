/**
 * @author rshirts
 * @createdOn 2/27/2023 at 5:31 PM
 * @projectName LibraryV4
 * @packageName csc150.library.views;
 */
package csc150.library.views;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LibraryUI {

    /**
     * Make the gui window for the openLibrary clint
     */
    public void makeWindow() {

        //makes the window
        JFrame window = new JFrame("OpenLibrary Client");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // makes the initial ui
        createInitUi(window);

        // sets the windows size and makes it visible
        window.setSize(700, 700);
        window.setLocationRelativeTo(null);
        window.setVisible(true);
    }


    /**
     * creates the initial ui for the
     * @param window the window that the ui is being added to
     */
    private void createInitUi(final JFrame window){
        JPanel panel = new JPanel();
        LayoutManager layout = new FlowLayout();
        panel.setLayout(layout);

        // makes the elements
        JLabel welcome = new JLabel("Welcome to the openLibrary Client");
        JTextField textField = new JTextField("Search for a book");

        JButton personalLibrary = new JButton("Go to personal library");
        personalLibrary.addActionListener(e -> {
            // update ui to go to personal library and display that
        });

        JButton search = new JButton("Search");
        search.addActionListener(e -> {
           // use the client to search for a book and display the information
        });

        // adds elements to panel
        panel.add(welcome);
        panel.add(textField);
        panel.add(search);
        panel.add(personalLibrary);

        // adds ui content to window
        window.getContentPane().add(panel, BorderLayout.CENTER);
    }
}

