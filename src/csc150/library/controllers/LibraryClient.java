/**
 * @author rshirts
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

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import csc150.library.models.Book;
import csc150.library.models.KeyPossibilities;

public class LibraryClient {
    public final String CLIENT_ADDRESS = "https://openlibrary.org/";

    public List<Book> getBookByTitleSearch(List<KeyPossibilities> keys, String title) {
        String pageContent = getPageContent("search.json?title=" + title);
        System.out.println(pageContent);
        return formatPageContent(keys, pageContent);
    }

    public Book getBookByISBN(List<KeyPossibilities> keys, String isbn) {
        //TODO: add checkISBN function
        String pageContent = getPageContent("search.json?q=" + isbn);
        return formatPageContent(keys, pageContent).get(0);
    }

    private Book getBookFromJson(List<KeyPossibilities> keys, JsonNode jsonNode) {
        Book book = new Book();
        for (KeyPossibilities key : keys) {
            switch (key) {
                case TITLE -> book.setTitle(jsonNode.get(KeyPossibilities.TITLE.toString()).asText());
                case FIRST_PUBLISH_YEAR -> book.setFirstPublishYear(jsonNode.get(
                        KeyPossibilities.FIRST_PUBLISH_YEAR.toString()).asInt());
                case NUMBER_OF_PAGES_MEDIAN -> book.setMedianPages(jsonNode.get(
                        KeyPossibilities.NUMBER_OF_PAGES_MEDIAN.toString()).asInt());
                case SUBJECT -> {
                    ArrayList<String> subjectsAsString = new ArrayList<>();
                    JsonNode subjectsAsJson = jsonNode.get(KeyPossibilities.SUBJECT.toString());
                    for (JsonNode subject : subjectsAsJson) {
                        subjectsAsString.add(subject.asText());
                    }
                    book.setSubjects(subjectsAsString);
                }
                case AUTHOR_NAME -> {
                    ArrayList<String> authorsAsString = new ArrayList<>();
                    JsonNode authorsAsJson = jsonNode.get(KeyPossibilities.AUTHOR_NAME.toString());
                    for (JsonNode author : authorsAsJson) {
                        authorsAsString.add(author.asText());
                    }
                    book.setAuthorNames(authorsAsString);
                }
                case PUBLISHER -> {
                    ArrayList<String> publisherAsString = new ArrayList<>();
                    JsonNode publisherAsJson = jsonNode.get(KeyPossibilities.PUBLISHER.toString());
                    for (JsonNode author : publisherAsJson) {
                        publisherAsString.add(author.asText());
                    }
                    book.setAuthorNames(publisherAsString);
                }
            }
        }
        return book;
    }

    private List<Book> formatPageContent(List<KeyPossibilities> keys, String pageContent) {
        try {
            List<Book> books = new ArrayList<>();
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(pageContent);
            if (jsonNode.has("docs")) {
                JsonNode docs = jsonNode.get("docs");
                for (JsonNode doc : docs) {
                    books.add(getBookFromJson(keys, doc));
                }
            } else {
                books.add(getBookFromJson(keys, jsonNode));
            }
            return books;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            System.out.println("fuck");
            return null;
        }
    }

    private String getPageContent(String urlString) {
        try {
            //create an url object for request to hit
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
            return response.toString();
        } catch(Exception ex) {
            return null;
        }
    }
}
