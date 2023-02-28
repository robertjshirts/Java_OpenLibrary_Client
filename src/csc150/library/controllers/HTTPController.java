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
import java.util.HashMap;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import csc150.library.models.Book;

public class HTTPController {

    public void run() {
        List<Book> rsurs = searchByTitle("red seas under red skies");
        for (Book rsur : rsurs) {
            System.out.println(rsur.toString());
        }
    }


    public void getAuthor(String author){
        // Replaces all spaces with %20 for search query
        author = author.replace(" ", "%20");
    }

    /**
     * searches openlibray for a book title
     * @param searchTitle the title of the book the user is trying to find
     */
    public List<Book> searchByTitle(String searchTitle) {
        searchTitle = searchTitle.replace(" ", "+");
        System.out.println(searchTitle);
        return makeRequest("title=" + searchTitle);
    }

    /**
     * make a request to the openlibrary api
     * @param query the type of search you want to request from the openlibrary api
     * @return the json request gotten from the openlibrary api formatted as a string
     */
    private List<Book> makeRequest(String query){
        try {
            // Create a URL object for the endpoint you want to hit
            System.out.println("https://openlibrary.org/search.json?" + query);
            URL url = new URL("https://openlibrary.org/search.json?" + query);

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

            //initialize JSON node from response
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(response.toString());
            JsonNode docs = jsonNode.get("docs");

            List<Book> booksFoundAsObject = new ArrayList<>();

            //get data from JSON node
            for (JsonNode doc : docs) {
                String title = null;
                String authorName = null;
                int publishYear = -1;
                int medianPages = -1;

                //have to wrap in try block if the key doesn't exist
                try {
                    title = doc.get("title").asText();
                } catch (Exception ignored) { }

                try {
                    publishYear = doc.get("first_publish_year").asInt();
                } catch (Exception ignored) { }

                try {
                    authorName = doc.get("author_name").get(0).asText();
                } catch (Exception ignored) { }

                try {
                    medianPages = doc.get("number_of_pages_median").asInt();
                } catch (Exception ignored) { }

                List<String> subjects = new ArrayList<>();
                 try {
                     for (JsonNode subject : doc.get("subject")) {
                         subjects.add(subject.asText());
                     }
                 } catch (Exception ignored) { }

                //add the book with the data to the list of books
                booksFoundAsObject.add(new Book(title, authorName, publishYear, medianPages, subjects));
            }

            //return the JSON response as a list of books
            return booksFoundAsObject;

        } catch (Exception e) {
            //prints out an error message and returns a null object
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }
}
