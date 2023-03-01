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
import csc150.library.models.QueryPossibilities;

public class LibraryClient {
    // API Address (no key necessary)
    public final String CLIENT_ADDRESS = "https://openlibrary.org/";

    //region Fetch and Parse

    /**
     * Parses the search result data, pulls the primary edition key and authors, then
     * parses the data from the primary edition, and stores all of that data in a book
     * object, then stores those book objects in a list
     * @param keys the keys to retrieve data from (ie publisher, title, etc)
     * @param searchResult the result from the initial search
     * @return a list of books with requested data
     */
    private List<Book> formatPageContent(List<KeyPossibilities> keys, String searchResult) {
        //Initialize book list
        List<Book> books = new ArrayList<>();
        try {

            // Map searchResult to a JsonNode
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode searchResultJson = objectMapper.readTree(searchResult);

            // Loop through each result
            JsonNode docs = searchResultJson.get("docs");
            for (JsonNode doc : docs) {
                // Have to fetch authorName here
                List<String> authors;
                try {
                    authors = new ArrayList<>();
                    for (JsonNode author : doc.get("author_name")) {
                        authors.add(author.asText());
                    }
                } catch (NullPointerException e) {
                    authors = null;
                }

                // Fetch primary edition key, ensure it isn't null
                String primaryKey = getPrimaryKey(doc);
                if (primaryKey == null) continue;

                // Fetch primary edition json and convert to JsonNode
                String primaryBookResult = getPageContent("books/" + primaryKey + ".json");
                JsonNode primaryBookJson = objectMapper.readTree(primaryBookResult);

                // Get Book info from primary edition json and add to books list
                books.add(getBookInfoFromJson(keys, primaryBookJson, primaryKey, authors));
            }
        } catch (Exception e) {

            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        // Return books list
        return books;
    }

    /**
     * Parses certain book data from a jsonNode of the primary edition of a book (the first released version,
     * in the original language). Also passes primaryKey and authors, because that can often not be parsed
     * from the primary edition book page
     * @param keys the keys to retrieve data from (ie publisher, title, etc)
     * @param jsonNode the book info in jsonNode format
     * @param primaryKey the primary edition key
     * @param authors list of authors
     * @return book object with requested data
     */
    private Book getBookInfoFromJson(List<KeyPossibilities> keys, JsonNode jsonNode, String primaryKey, List<String> authors) {
        // Initialize book object
        Book book = new Book();

        // Get basic information
        try {
            book.setPrimaryKey(primaryKey);
            book.setPrimaryIsbn(jsonNode.get("isbn_13").asText());
            book.setImageId(jsonNode.get("covers").get(0).asText());
        } catch (Exception ignored) { }

        // Get requested information
        for (KeyPossibilities key : keys) {
            try {
                switch (key) {
                    case AUTHOR_NAME -> book.setAuthorNames(authors);
                    case SUMMARY -> book.setSummary(jsonNode.get("description").get("value").asText());
                    case TITLE -> book.setTitle(jsonNode.get("title").asText());
                    case PUBLISH_DATE -> book.setPublishDate(jsonNode.get("publish_date").asText());
                    case PUBLISHERS -> {
                        ArrayList<String> publishersAsString = new ArrayList<>();
                        JsonNode publishers = jsonNode.get("publishers");
                        for (JsonNode publisher : publishers) {
                            publishersAsString.add(publisher.asText());
                        }
                        book.setPublishers(publishersAsString);
                    }
                    case NUMBER_OF_PAGES -> book.setNumberOfPages(jsonNode.get("number_of_pages").asInt());
                }
            } catch (Exception ignored) { }
        }

        // Return book object
        return book;
    }

    /**
     * Retrieves the primary edition key from a book (the first released version,
     * in the original language) in jsonNode format
     * @param jsonNode the book information
     * @return the primary edition key for the book
     */
    private String getPrimaryKey(JsonNode jsonNode) {
        // Get primary edition key, or return null if one doesn't exist
        // This helps vet books that aren't substantial
        try {
            return jsonNode.get("cover_edition_key").asText();
        } catch (Exception ignored) { return null; }
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

    //endregion

    //region Interact and Search

    /**
     * Advanced search method that every other search calls
     * types and queries must be "linked" ie type at index 0 is searching with queries at index 0
     * @param types list of the types of queries to search for
     * @param queries list of queries to search for
     * @param keys the keys that you want to get data from (ie publisher, page count, etc)
     * @return a list of books from the search with the keys data assigned
     */
    public List<Book> advancedSearch(List<QueryPossibilities> types, List<String> queries, List<KeyPossibilities> keys) {
        if (types == null || queries == null || keys == null ||
                types.size() != queries.size() || keys.isEmpty()) return null;
        String searchResult = getPageContent(queryFormatter(types, queries));
        return formatPageContent(keys, searchResult);
    }

    /**
     * Searches OpenLibrary for books based on an author and returns certain data
     * @param keys the data you want from each book
     * @param author the author you are searching for
     * @return a lst of Book objects with the requested data
     */
    public List<Book> authorSearch(List<KeyPossibilities> keys, String author) {
        // Ensure search term is valid
        if (author == null || author.isBlank()) {
            return null;
        }
        // Returns book list using advancedSearch method
        return advancedSearch(List.of(QueryPossibilities.AUTHOR), List.of(author), keys);
    }

    /**
     * Searches OpenLibrary for books based on an author and returns all relevant data
     * @param author the author you are searching for
     * @return a lst of Book objects
     */
    public List<Book> authorSearch(String author) {
        return authorSearch(List.of(KeyPossibilities.values()), author);
    }

    /**
     * Searches OpenLibrary for a book based on a title and returns certain data
     * @param keys the data you want from each book
     * @param title the title you are searching for
     * @return a list of Book objects with the requested data
     */
    public List<Book> titleSearch(List<KeyPossibilities> keys, String title) {
        // Ensure search term is valid
        if (title == null || title.isBlank()) {
            return null;
        }

        // Returns book list using advancedSearch method
        return advancedSearch(List.of(QueryPossibilities.TITLE), List.of(title), keys);
    }

    /**
     * Searches OpenLibrary for a book based on a title and all relevant data
     * @param title the title you are searching for
     * @return a list of Book objects
     */
    public List<Book> titleSearch(String title) {
        // Calls the original function but with all KeyPossibilities
        return titleSearch(List.of(KeyPossibilities.values()), title);
    }

    /**
     * Searches OpenLibrary for a book based on the ISBN and returns certain data
     * @param keys the data you want from the book
     * @param isbn the isbn you are searching for
     * @return a Book object with the requested data
     */
    public Book isbnSearch(List<KeyPossibilities> keys, String isbn) {
        // Ensure search term is valid
        if (isbn == null || isbn.isBlank() || !checkISBN(isbn)) {
            return null;
        }

        // Returns first book from book list using advancedSearch method (because we expect one result from isbn)
        return Objects.requireNonNull(
                advancedSearch(List.of(QueryPossibilities.ISBN), List.of(isbn), keys)).get(0);
    }

    /**
     * Searches OpenLibrary for a book based on the ISBN and returns all relevant data
     * @param isbn the isbn you are searching for
     * @return a Book object
     */
    public Book isbnSearch(String isbn) {
        return isbnSearch(List.of(KeyPossibilities.values()), isbn);
    }

    /**
     * Formats list of types of queries and query values to a proper subdomain
     * @param types types of queries
     * @param queries query values
     * @return the formatted subdomain as a string
     */
    private String queryFormatter(List<QueryPossibilities> types, List<String> queries) {
        // Initialize StringBuilder
        StringBuilder formattedQuery = new StringBuilder();
        formattedQuery.append("search.json?");

        // For each query type, add the associated query and a
        for (int i = 0; i < types.size(); i++) {
            switch (types.get(i)) {
                case ISBN -> formattedQuery.append("q=").append(queries.get(i));
                case TITLE -> formattedQuery.append("title=").append(queries.get(i));
                case AUTHOR -> formattedQuery.append("author=").append(queries.get(i));
                case SUBJECT -> formattedQuery.append("subject=").append(queries.get(i));
                case PERSON -> formattedQuery.append("person=").append(queries.get(i));
                case PLACE -> formattedQuery.append("place=").append(queries.get(i));
                case PUBLISHER -> formattedQuery.append("publisher=").append(queries.get(i));
            }

            // Append '&' to signify new search argument
            formattedQuery.append("&");
        }

        // Append sort by # of editions (shows more relevant results first)
        formattedQuery.append("sort=editions");

        // Return formatted query, and replace any " " with +
        return formattedQuery.toString().replace(" ", "+");
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
    //endregion
}
