/**
 * @author rshirts dthompson
 * @createdOn 2/27/2023 at 5:31 PM
 * @projectName LibraryV4
 * @packageName csc150.library.views;
 */
package csc150.library.views;

import csc150.library.Main;
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
    FileController files = new FileController();

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
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                //makes window for personal library
                JFrame personalLibrary = new JFrame("Personal library");
                personalLibrary.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

                //makes panels and set the layouts
                JPanel mainPanel = new JPanel();
                mainPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
                JPanel contentPanel = new JPanel();
                contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
                JScrollPane scrollPane = new JScrollPane(mainPanel);

                //gets the content for the window
                JLabel contentLabel1 = new JLabel(FileController.FAVORITES);
                JTextArea contentTextArea1 = new JTextArea();
                if (files.doesFileExist(FileController.FAVORITES)){
                    contentTextArea1.setText(files.readFile(FileController.FAVORITES));
                }
                else{
                    contentTextArea1.setText("");
                }
                JLabel contentLabel2 = new JLabel(FileController.READING);
                JTextArea contentTextArea2 = new JTextArea();
                if (files.doesFileExist(FileController.READING)){
                    contentTextArea2.setText(files.readFile(FileController.READING));
                }
                else{
                    contentTextArea2.setText("");
                }
                JLabel contentLabel3 = new JLabel(FileController.HAS_READ);
                JTextArea contentTextArea3 = new JTextArea();
                if (files.doesFileExist(FileController.HAS_READ)){
                    contentTextArea3.setText(files.readFile(FileController.HAS_READ));
                }
                else{
                    contentTextArea3.setText("");
                }
                JLabel contentLabel4 = new JLabel(FileController.PLAN_TO_READ);
                JTextArea contentTextArea4 = new JTextArea();
                if (files.doesFileExist(FileController.PLAN_TO_READ)){
                    contentTextArea4.setText(files.readFile(FileController.PLAN_TO_READ));
                }
                else{
                    contentTextArea4.setText("");
                }

                //creates an exit button
                JButton exit = new JButton("Exit");
                exit.addActionListener(e -> {
                    personalLibrary.dispose();
                });

                JButton delete = new JButton();
                delete.addActionListener(e -> {
                    String whatLine = JOptionPane.showInputDialog("What is the title of the book you want to delete");
                    files.deleteFromFile(FileController.FAVORITES, whatLine);
                    contentTextArea1.revalidate();
                });
                mainPanel.add(delete);


                //sets the content to the panels
                contentPanel.add(contentLabel1);
                contentPanel.add(contentTextArea1);
                contentPanel.add(contentLabel2);
                contentPanel.add(contentTextArea2);
                contentPanel.add(contentLabel3);
                contentPanel.add(contentTextArea3);
                contentPanel.add(contentLabel4);
                contentPanel.add(contentTextArea4);
                contentPanel.add(exit);
                mainPanel.add(contentPanel);

                //set the panels to the window
                personalLibrary.getContentPane().add(scrollPane);

                //sets the size and makes the window visible
                personalLibrary.setExtendedState(JFrame.MAXIMIZED_BOTH);
                personalLibrary.setVisible(true);
            }
        });
    }

    /**
     * Makes a window showing all search results pulled from the openLibrary api
     * @param search the term the user is using to search through the openLibrary api
     */
    private void displaySearch(String search){
        search = search.trim();

        // get a list of books from the library client
        LibraryClient client = new LibraryClient();
        List<Book> books = client.titleSearch(search);

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
                JScrollPane scrollPane = new JScrollPane(mainPanel);

                //gets the search for the book and displays them
                for (int i = 0; i < books.size(); i++) {
                    JPanel buttonPanel = new JPanel();
                    buttonPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
                    JTextArea textArea = new JTextArea();
                    Book book = books.get(i);
                    textArea.setText("Title:" + book.getTitle() + "\n" + "Author: " + book.getAuthorNames() + "\n" + "Publisher: " + book.getPublisher() + "\n" + "Publish date: " + book.getPublishDate() + "\n" + "Number of pages: " + book.getNumberOfPages() + "\n\n");

                    //makes the buttons that allows you to save the book
                    JButton favorite = new JButton("Add to favorites");
                    favorite.addActionListener(e -> {
                        boolean append = files.doesFileExist(FileController.FAVORITES);
                        files.writeFile(FileController.FAVORITES, textArea.getText(), append);
                    });
                    JButton reading = new JButton("Add to reading");
                    reading.addActionListener(e -> {
                        boolean append = files.doesFileExist(FileController.READING);
                        files.writeFile(FileController.READING, textArea.getText(), append);
                    });
                    JButton hasRead = new JButton("Add to Has Read");
                    hasRead.addActionListener(e -> {
                        boolean append = files.doesFileExist(FileController.HAS_READ);
                        files.writeFile(FileController.HAS_READ, textArea.getText(), append);
                    });
                    JButton planToRead = new JButton("Add to Plan To Read");
                    planToRead.addActionListener(e -> {
                        boolean append = files.doesFileExist(FileController.PLAN_TO_READ);
                        files.writeFile(FileController.PLAN_TO_READ, textArea.getText(), append);
                    });

                    //set all elements to the panel
                    buttonPanel.add(favorite);
                    buttonPanel.add(reading);
                    buttonPanel.add(hasRead);
                    buttonPanel.add(planToRead);
                    mainPanel.add(textArea);
                    mainPanel.add(buttonPanel);
                }

                //creates an exit button
                JButton exit = new JButton("Exit");
                exit.addActionListener(e -> {
                    window.dispose();
                });

                //makes a new panel just to put the exit button on the left :(
                JPanel buttonPanel = new JPanel();
                buttonPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
                buttonPanel.add(exit);
                mainPanel.add(buttonPanel);

                //set panel to the window
                window.getContentPane().add(scrollPane);

                //sets the size and makes window visible
                window.setExtendedState(JFrame.MAXIMIZED_BOTH);
                window.setVisible(true);
            }
        });
    }
}