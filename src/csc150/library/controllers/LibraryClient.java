/**
 * @author rshirts dthompson
 * @createdOn 2/27/2023 at 12:57 PM
 * @projectName LibraryV4
 * @packageName csc150.library.controllers;
 */
package csc150.library.controllers;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import csc150.library.models.Book;
import csc150.library.models.KeyPossibilities;

public class LibraryClient {
    public final String CLIENT_ADDRESS = "https://openlibrary.org/";

    public List<Book> getBookByAuthorSearch(List<KeyPossibilities> keys, String author) {
        // Retrieve page content based on formatted search
//        String pageContent = getPageContent("search.json?author");
        return null;
    }

    /**
     * Searches OpenLibrary for a book based on a title and returns certain data
     * @param keys the data you want from each book
     * @param title the title you are searching for
     * @return a list of Book objects with the requested key data assigned to each
     */
    public List<Book> getBookByTitleSearch(List<KeyPossibilities> keys, String title) {
        // Ensure search term is valid
        if (title == null || title.isBlank()) {
            return null;
        }
        // Retrieve page content based on formatted search
        String pageContent = getPageContent("search.json?title=" + title.replace(" ", "+") + "&sort=editions");
        // Return array
        return formatPageContent(keys, pageContent);
    }

    /**
     * Searches OpenLibrary for a book based on a title and all relevant data
     * @param title the title you are searching for
     * @return a list of Book objects
     */
    public List<Book> getBookByTitleSearch(String title) {
        // Calls the original function but with all KeyPossibilities
        return (getBookByTitleSearch(List.of(KeyPossibilities.values()), title));
    }

    /**
     * Searches OpenLibrary for a book based on the ISBN and returns certain data
     * @param keys the data you want from the book
     * @param isbn the isbn you are searching for
     * @return a Book object with the requested key data
     */
    public Book getBookByISBN(List<KeyPossibilities> keys, String isbn) {
        // Ensure search term is valid
        if (isbn == null || isbn.isBlank() || !checkISBN(isbn)) {
            return null;
        }

        // Retrieve page content based on formatted search
        String pageContent = getPageContent("search.json?q=" + isbn.trim());
        // Get the first (and only) element from the returned array
        return Objects.requireNonNull(formatPageContent(keys, pageContent)).get(0);
    }

    /**
     * Searches OpenLibrary for a book based on the ISBN and returns all relevant data
     * @param isbn the isbn you are searching for
     * @return a Book object
     */
    public Book getBookByISBN(String isbn) {
        return getBookByISBN(List.of(KeyPossibilities.values()), isbn);
    }

    /**
     * Takes page content and returns a list of books with requested data
     * @param keys the data you want from each book
     * @param pageContent the JSON page content to parse
     * @return a list of books with the requested data
     */
    private List<Book> formatPageContent(List<KeyPossibilities> keys, String pageContent) {
        try {
            // Initialize book array
            List<Book> books = new ArrayList<>();

            // Initialize and read JsonNode
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(pageContent);

            // Parse the search data and assign to Book object
            JsonNode docs = jsonNode.get("docs");
            for (JsonNode doc : docs) {
                books.add(getBookFromJson(keys, doc));
            }

            // Return the array of books
            return books;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return null;
        }
    }

    /**
     * Contacts OpenLibrary server and requests a JSON
     * @param urlString the subdirectory you want
     * @return the received page content as a string
     */
    private String getPageContent(String urlString) {
        try {
            // Create an url object for request to hit
            System.out.println(CLIENT_ADDRESS + urlString);
            URL url = new URL(CLIENT_ADDRESS + urlString);

            // Open a connection to the URL
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // Set the request method to GET
            connection.setRequestMethod("GET");

            // Set the Accept header to "application/json"
            connection.setRequestProperty("Accept", "application/json");

            // Read the response
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            //Return the response
            return response.toString();
        } catch(Exception ex) {
            return null;
        }
    }

    /**
     * Parses certain data from JsonNode object
     * @param keys the data you want to parse
     * @param jsonNode the data you are parsing, as a JSON
     * @return A Book object with the parsed data assigned
     */
    private Book getBookFromJson(List<KeyPossibilities> keys, JsonNode jsonNode) {
        // Initialize book object
        Book book = new Book();
        // Loop through all keys requested
        for (KeyPossibilities key : keys) {
            // Retrieve data based on key type
            switch (key) {
                // Sets title to the key value "title"
                case TITLE -> {
                    try {
                        book.setTitle(jsonNode.get(KeyPossibilities.TITLE.toString()).asText());
                    } catch (Exception ignored) { }
                }

                // Sets publish year to key value "first_publish_year"
                case FIRST_PUBLISH_YEAR -> {
                    try {
                        book.setFirstPublishYear(jsonNode.get(
                                KeyPossibilities.FIRST_PUBLISH_YEAR.toString()).asInt());
                    } catch (Exception ignored) { }
                }

                // Sets median pages to key value "number_of_pages_median"
                case NUMBER_OF_PAGES_MEDIAN -> {
                    try {
                        book.setMedianPages(jsonNode.get(
                                KeyPossibilities.NUMBER_OF_PAGES_MEDIAN.toString()).asInt());
                    } catch (Exception ignored) { }
                }

                // Sets subject list equal to all values under key "subject"
                case SUBJECT -> {
                    try {
                            ArrayList<String> subjectsAsString = new ArrayList<>();
                            JsonNode subjectsAsJson = jsonNode.get(KeyPossibilities.SUBJECT.toString());
                            for (JsonNode subject : subjectsAsJson) {
                                subjectsAsString.add(subject.asText());
                            }

                        // Assigns list to book object
                        book.setSubjects(subjectsAsString);
                        } catch (Exception ignored) { }
                }

                // Sets author list equal to all values under key "author_name"
                case AUTHOR_NAME -> {
                    try {
                        ArrayList<String> authorsAsString = new ArrayList<>();
                        JsonNode authorsAsJson = jsonNode.get(KeyPossibilities.AUTHOR_NAME.toString());
                        for (JsonNode author : authorsAsJson) {
                            authorsAsString.add(author.asText());
                        }

                        // Assigns list to book object
                        book.setAuthorNames(authorsAsString);
                    } catch (Exception ignored) { }
                }

                // Sets publisher list equal to all values under key "publisher"
                case PUBLISHER -> {
                    try {
                        ArrayList<String> publisherAsString = new ArrayList<>();
                        JsonNode publisherAsJson = jsonNode.get(KeyPossibilities.PUBLISHER.toString());
                        for (JsonNode publisher : publisherAsJson) {
                            publisherAsString.add(publisher.asText());
                        }

                        // Assigns list to book object
                        book.setPublisher(publisherAsString);
                    } catch (Exception ignored) { }
                }
            }
        }
        return book;
    }

    /**
     * Checks that isbn is valid
     * @param isbn the isbn (10 or 13 digit, with or without dashes or spaces)
     * @return true if valid, false if not
     */
    private boolean checkISBN(String isbn) {
        if (isbn == null || isbn.length() == 0) {
            return false;
        }

        // Remove any dashes or spaces from the string
        isbn = isbn.replaceAll("[-\\s]", "");

        int len = isbn.length();

        // Check if the string is a valid ISBN-10
        if (len == 10) {
            int sum = 0;
            for (int i = 0; i < len; i++) {
                char c = isbn.charAt(i);
                if (!Character.isDigit(c)) {
                    return false;
                }
                sum += (10 - i) * Character.getNumericValue(c);
            }
            return sum % 11 == 0;
        }

        // Check if the string is a valid ISBN-13
        if (len == 13) {
            int sum = 0;
            for (int i = 0; i < len; i++) {
                char c = isbn.charAt(i);
                if (!Character.isDigit(c)) {
                    return false;
                }
                int digit = Character.getNumericValue(c);
                sum += (i % 2 == 0) ? digit : 3 * digit;
            }
            return sum % 10 == 0;
        }

        return false;
    }
}
