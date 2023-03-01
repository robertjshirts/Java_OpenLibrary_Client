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
        createRootFolder();

        BufferedWriter write = null;
        try {
            write = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(getFileName(fileName), append)));
            write.write(contents);
        } catch (Exception ignored) {

        } finally {
            closeStream(write);
        }
    }

    /**
     * returns the content that is stored in fileName
     * @param fileName the name of the file you want to read from
     * @return the content as a string
     */
    public String readFile(String fileName) {
        BufferedReader read = null;
        StringBuilder content = new StringBuilder();
        try {
            read = new BufferedReader(new InputStreamReader(new FileInputStream(getFileName(fileName))));
            while (read.ready()) {
                content.append(read.readLine()).append("\r\n");
            }
        } catch (Exception ignored) {
        } finally {
            closeStream(read);
        }
        return content.toString();
    }

    /**
     * Deletes title line and 4 lines of data after it from fileName
     * @param fileName the file you are changing
     * @param title the title of the book data you want to delete
     */
    public void deleteFromFile(String fileName, String title) {
        title = "Title:" + title;
        String content = readFile(fileName);
        int startIndex = content.indexOf(title);
        if (startIndex == -1) return;
        int endIndex = startIndex;
        int newLineCount = 0;
        while (true) {
            try {
                if (content.charAt(endIndex++) == '\n') {
                    System.out.println("found newline");
                    newLineCount++;
                    if (newLineCount == 5) {
                        writeFile(fileName, content.replace(content.substring(startIndex, endIndex+2), ""), false);
                        return;
                    }
                }
            } catch (IndexOutOfBoundsException e) {
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
        File rootFolder = new File(ROOT_FOLDER);
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
        createRootFolder();
        File rootFolder = new File(ROOT_FOLDER);
        List<String> files = Arrays.asList(rootFolder.list());
        for (int i = 0; i < files.size(); i++) {
           if (files.get(i).equals(fileName + ".txt")){
               return true;
           }
        }
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