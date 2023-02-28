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

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import csc150.library.models.Book;

public class HTTPController {
    //TODO: Restructure this whole ass thing i fucked it up lmao

    public void run() {
        List<Book> result = searchByAuthor("Scott Lynch");
        for (Book res : result) {
            System.out.println(res.toString());
        }
        System.out.println(searchByISBN("0451526538"));
    }

    public Book searchByISBN(String query) {

//        List<Book> books = makeRequestBySearch("isbn/" + query + ".json");
        return null;
    }

    /**
     * searches opel library for an author
     * @param query the author the user is searching for
     * @return the search result as a list of books
     */
    public List<Book> searchByAuthor(String query){
        query = query.replace(" ", "%20");
        System.out.println(query);
//        return makeRequestBySearch("search.json?author="+query);
        return null;
    }

    /**
     * searches open library for a book title
     * @param query the title of the book the user is trying to find
     * @return the search result as a list of books
     */
    public List<Book> searchByTitle(String query) {
        query = query.replace(" ", "+");
        System.out.println(query);
//        return makeRequestBySearch("search.json?title=" + query);
        return null;
    }

    /**
     * make a request to the open library api
     * @param query the formatted queries to add at the end of the search
     * @return the json request gotten from the open library api formatted as a list of book objects
     */
    private List<Book> makeRequestBySearch(String query, String[] keys){
        try {
            //get page content from url
            String response = getPageContent(query);

            //initialize JSON node from response
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(response);
            JsonNode docs = jsonNode.get("docs");

            List<Book> booksFoundAsObject = new ArrayList<>();

            //get data from JSON node
            for (JsonNode doc : docs) {
                String title = null;
                String authorName = null;
                int publishYear = -1;
                int medianPages = -1;

                //have to wrap in try block if the key doesn't exist
//                for (String key : keys) {
//                    try {
//                        doc.get(key).asText();
//                    } catch (Exception ignored) { }
//                }
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

    private String getPageContent(String urlString) throws IOException {
        //create an url object for request to hit
        System.out.println("https://openlibrary.org/" + urlString);
        URL url = new URL("https://openlibrary.org/" + urlString);
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
    }
}
