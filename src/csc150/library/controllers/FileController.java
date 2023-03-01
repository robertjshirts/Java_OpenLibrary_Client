/**
 * @author rshirts dthompson
 * @createdOn 2/27/2023 at 4:45 PM
 * @projectName LibraryV4
 * @packageName csc150.library.models;
 */
package csc150.library.controllers;

import java.io.*;
import java.util.Arrays;
import java.util.List;

public class FileController {
    //names of the files and root folder
    public static final String ROOT_FOLDER = "BookShelf";
    public static final String FAVORITES = "Favorites";
    public static final String HAS_READ = "Has+Read";
    public static final String PLAN_TO_READ = "Plan+To+Read";
    public static final String READING = "Reading";

    /**
     * Writes to fileName with contents, if append is false it writes over the file
     * @param fileName the name of the file you want to write to
     * @param contents the contents you want to write to the file
     * @param append if you want to append to or rewrite the file
     */
    public void writeFile(String fileName, String contents, boolean append) {
        // Create root folder if it doesn't exist
        createRootFolder();

        // Declare writer
        BufferedWriter write = null;
        try {
            // Assign writer
            write = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(getFileName(fileName), append)));

            // Write the contents to the file
            write.write(contents);
        } catch (Exception ignored) {

        } finally {
            // Close the writer
            closeStream(write);
        }
    }

    /**
     * returns the content that is stored in fileName
     * @param fileName the name of the file you want to read from
     * @return the content as a string
     */
    public String readFile(String fileName) {
        // Declare reader and content
        BufferedReader read = null;
        StringBuilder content = new StringBuilder();
        try {
            // Initialize reader
            read = new BufferedReader(new InputStreamReader(new FileInputStream(getFileName(fileName))));

            // Read through all lines one at a time and append them to content with newline characters
            while (read.ready()) {
                content.append(read.readLine()).append("\r\n");
            }
        } catch (Exception ignored) {
        } finally {
            // Close the reader
            closeStream(read);
        }

        // Return the content
        return content.toString();
    }

    /**
     * Deletes title line and 4 lines of data after it from fileName
     * @param fileName the file you are changing
     * @param title the title of the book data you want to delete
     */
    public void deleteFromFile(String fileName, String title) {
        final int LINES_OF_DATA = 5;
        if (title == null) return;
        // Format title
        title = "Title:" + title;

        //Load content
        String content = readFile(fileName);

        // Find the start of the title
        int startIndex = content.indexOf(title);

        // Return if not found
        if (startIndex == -1) return;

        // Declare and assign endIndex variable and newLineCount
        int endIndex = startIndex;
        int newLineCount = 0;

        while (true) {
            try {
                // Start searching for new line characters char by char, incrementing the endIndex variable
                if (content.charAt(endIndex++) == '\n') {
                    // Increment newLineCount
                    newLineCount++;

                    // If we find enough newline characters
                    if (newLineCount == LINES_OF_DATA) {
                        // Replace substring of startIndex to endIndex with nothing, and rewrite the file
                        writeFile(fileName, content.replace(content.substring(startIndex, endIndex+2), ""), false);
                        return;
                    }
                }
            } catch (IndexOutOfBoundsException e) {
                // If there aren't enough newLine characters
                return;
            }
        }
    }

    /**
     * Gets the path of a file you are trying to use
     * @param fileName the name of the file you are trying to use
     * @return the path of the file you are trying to use
     */
    private String getFileName(String fileName){
        // file path
        return ROOT_FOLDER + "\\" + fileName + ".txt";
    }

    /**
     * Will make a root folder if does not exist
     */
    private void createRootFolder(){

        //makes a file
        File rootFolder = new File(ROOT_FOLDER);

        //checks in the folder exists
        if(!rootFolder.exists()){

            //makes directory
            try {
                rootFolder.mkdir();
            }catch (SecurityException e){
                System.out.println(e.getMessage());
            }
        }
    }

    /**
     * Checks if a file exits or no in the folder
     * @param fileName the name of the file you want to see exits
     * @return a true or false value for if the file exits
     */
    public boolean doesFileExist(String fileName){
        //checks if root folder exists
        createRootFolder();

        // take all files in the root folder and puts them in an array
        File rootFolder = new File(ROOT_FOLDER);
        String[] files = rootFolder.list();

        //check if array is null
        assert files != null;

        //checks if file is in the list
        for (String file : files) {
            if (file.equals(fileName + ".txt")) {

                //file was found
                return true;
            }
        }

        //file wasn't found
        return false;
    }

    /**
     * Closes the read or write stream with error handling and such
     * @param stream the stream to close
     */
    private void closeStream(Closeable stream) {
        if (stream == null) return;
        try {
            stream.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}