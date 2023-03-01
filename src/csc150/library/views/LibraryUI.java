/**
 * @author rshirts dthompson
 * @createdOn 2/27/2023 at 5:31 PM
 * @projectName LibraryV4
 * @packageName csc150.library.views;
 */
package csc150.library.views;

import csc150.library.controllers.FileController;
import csc150.library.controllers.LibraryClient;
import csc150.library.models.Book;
import javax.swing.*;
import java.awt.*;
import java.util.List;

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
        JPanel mainPanel = new JPanel(new FlowLayout());
        JPanel inputPanel = new JPanel();
        JPanel buttonPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        inputPanel.setLayout(new FlowLayout());
        buttonPanel.setLayout(new FlowLayout());

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
        buttonPanel.add(personalLibrary);
        mainPanel.add(welcome);
        mainPanel.add(inputPanel);
        mainPanel.add(buttonPanel);

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

                //makes panels and sets the layout
                JPanel mainPanel = new JPanel();
                mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
                JScrollPane scrollPane = new JScrollPane(mainPanel);

                //makes the content area for each file
                for (int i = 1; i < 5; i++) {

                    //makes the panels and sets the layout
                   JPanel panel = new JPanel();
                   panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
                   JPanel buttonPanel = new JPanel();
                   buttonPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

                   //gets the name of the content section
                    String fileName = switch (i){
                        default-> FileController.FAVORITES;
                        case 2-> FileController.READING;
                        case 3-> FileController.HAS_READ;
                        case 4-> FileController.PLAN_TO_READ;
                    };

                    //makes label, text area, and buttons for the content section
                    JLabel contentLabel = new JLabel(fileName.replace("+", " "));
                    contentLabel.setFont(new Font("Arial", Font.BOLD, 20));
                    JTextArea contentTextArea = new JTextArea();
                    contentTextArea.setEditable(false);
                    if (files.doesFileExist(fileName)){
                        contentTextArea.setText(files.readFile(FileController.FAVORITES));
                    }
                    else{
                        contentTextArea.setText("Empty");
                    }

                    JButton delete = new JButton("Delete");
                    delete.addActionListener(e -> {
                        String whatLine = JOptionPane.showInputDialog("What is the title of the book you want to delete");
                        files.deleteFromFile(fileName, whatLine.trim());
                        contentTextArea.setText(files.readFile(fileName));
                    });

                    JButton move = new JButton("Move");
                    move.addActionListener(e -> {

                    });

                    //sets the content to panels and adds them to main panel
                    mainPanel.add(contentLabel);
                    mainPanel.add(contentTextArea);
                    buttonPanel.add(delete);
                    buttonPanel.add(move);
                    mainPanel.add(buttonPanel);
                }

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
        List<Book> books = client.search(search);

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
                    textArea.setEditable(false);
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