/**
 * @author rshirts
 * @createdOn 2/27/2023 at 4:45 PM
 * @projectName LibraryV4
 * @packageName csc150.library.models;
 */
package csc150.library.controllers;

import java.io.*;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

public class FileController {

    //names of the files and root folder
    public static final String ROOT_FOLDER = "BookShelf";
    public static final String FAVORITES = "Favorites";
    public static final String HAS_READ = "HasRed";
    public static final String PLAN_TO_READ = "PlaneToRead";
    public static final String READING = "Reading";


    /**
     * write to a file
     * @param fileName the name of the file you would like to write to d
     * @param contents what will be written to the file
     * @param append are you going to append to an existing file
     */
    public void writeFile(String fileName, String contents, boolean append) {
        //checks if root folder exits
        createRootFolder();
        BufferedWriter write = null;
        try {
            //writes to the file
            write = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(getFileName(fileName), append)));
            write.write(contents);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            // closes the file
            try {
                if(write == null){
                    return;
                }
                write.close();
            } catch (IOException e) {
                throw new RuntimeException();
            }
        }
    }

    /**
     * Delete something from a file
     * @param fileName the name of the file you wish to delete something from
     * @param contentToDelete the content that you want to delete from the file
     */
    public void deleteFromFile(String fileName, String contentToDelete){
        BufferedReader read = null;
        String content = "";
        try{
            //reads the file per line and appends to a bufferedReader
            read = new BufferedReader(new InputStreamReader(new FileInputStream(getFileName(fileName))));
            String currentLine = "";
            //removes the line
            while ((currentLine = read.readLine()) != null) {
                if(currentLine.equals(contentToDelete)){
                    continue;
                }
                content += read.readLine() + "\r\n";
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                //closes the file
                read.close();
            }catch (IOException e){
                throw new RuntimeException(e.getMessage());
            }
        }
        //rewrite the file without the removed line
        writeFile(fileName, content, false);
    }

    /**
     * Reads the content of a file
     * @param fileName the name of the file you want to read
     * @return the contents of a file
     */
    public String readFile(String fileName){
        BufferedReader read = null;
        String content = "";
        try{
            //reads the file per line and appends to a bufferedReader
            read = new BufferedReader(new InputStreamReader(new FileInputStream(getFileName(fileName))));
            while (read.ready()) {
                content += read.readLine() + "\r\n";
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                //closes the file
                read.close();
            }catch (IOException e){
                throw new RuntimeException(e.getMessage());
            }
        }
        // returns content of file
        return content;
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
}