/**
 * @author rshirts
 * @createdOn 2/27/2023 at 5:31 PM
 * @projectName LibraryV4
 * @packageName csc150.library.views;
 */
package csc150.library.views;

import csc150.library.controllers.FileController;
import csc150.library.controllers.LibraryClient;
import csc150.library.controllers.MainController;
import csc150.library.models.Book;
import csc150.library.models.KeyPossibilities;

import javax.swing.*;
import java.awt.*;
import java.util.List;

import static csc150.library.models.KeyPossibilities.*;

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

        // creates panels and layouts
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setOpaque(true);
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new FlowLayout());

        // makes the elements
        JLabel welcome = new JLabel("Welcome to the openLibrary Client");
        JTextField textField = new JTextField("Search for a book");

        JButton personalLibrary = new JButton("Go to personal library");
        personalLibrary.addActionListener(e -> {
            // displays the window for personal library
            displayPersonalLibrary();
        });

        JButton search = new JButton("Search");
        search.addActionListener(e -> {
           // use the client to search for a book and display the information
            displaySearch(textField.getText());
        });

        // adds elements to panel
        inputPanel.add(textField);
        inputPanel.add(search);
        mainPanel.add(welcome);
        mainPanel.add(inputPanel);
        mainPanel.add(personalLibrary);

        // adds ui content to window
        window.getContentPane().add(mainPanel, BorderLayout.CENTER);
    }

    /**
     * Display your personal library
     */
    private void displayPersonalLibrary(){

        //makes a new runnable for the page
        FileController files = new FileController();
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                //makes window for personal library
                JFrame personalLibrary = new JFrame("Personal library");
                personalLibrary.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

                //makes panels and set the layouts
                JPanel mainPanel = new JPanel();
                mainPanel.setLayout(new FlowLayout());
                JPanel contentPanel = new JPanel();
                contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));

                //TODO add content from file manager here
                //gets the content for the window
                JLabel contentLabel1 = new JLabel(FileController.FAVORITES);
                contentLabel1.setBackground(Color.gray);
                JLabel contentLabel2 = new JLabel(FileController.READING);
                JLabel contentLabel3 = new JLabel(FileController.HASREAD);
                JLabel contentLabel4 = new JLabel(FileController.PLAN_TO_READ);

                //sets the content to the panels
                contentPanel.add(contentLabel1);
                contentPanel.add(contentLabel2);
                contentPanel.add(contentLabel3);
                contentPanel.add(contentLabel4);
                mainPanel.add(contentPanel);

                //set the panels to the window
                personalLibrary.getContentPane().add(mainPanel);

                //sets the size and makes the window visible
                personalLibrary.setSize(700, 700);
                personalLibrary.setVisible(true);
            }
        });
    }

    private void displaySearch(String search){
        LibraryClient client = new LibraryClient();
        List<Book> books = client.getBookByTitleSearch(search);
        for (int i = 0; i < books.size(); i++) {
            System.out.println(books.get(i).toString());
        }

        // makes a new runnable for the window
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {

                //makes the window
                JFrame window = new JFrame("Search results");
                window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

                //makes panel and set layout
                JPanel mainPanel = new JPanel();
                mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

                //gets the search for the book
                for (int i = 0; i < books.size(); i++) {
                    JTextArea textArea = new JTextArea();
                    //todo fix median pages
                    Book book = books.get(i);
                    textArea.setText("Title:" + book.getTitle() + "\n" + "Author: " + book.getAuthorNames() + "\n" + "Publisher: " + book.getPublisher() + "\n" + "Subjects: " + book.getSubjects() + "\n" + "Median pages" + "\n\n");
                    mainPanel.add(textArea);
                }

                JScrollPane scroller = new JScrollPane();
                scroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
                scroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
                mainPanel.add(scroller);

                //set panel to the window
                window.getContentPane().add(mainPanel);

                //sets the size and makes window visible
                window.setSize(700, 700);
                window.setVisible(true);
            }
        });
    }
}