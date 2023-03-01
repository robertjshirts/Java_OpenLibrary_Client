/**
 * @author rshirts dthompson
 * @createdOn 2/27/2023 at 1:09 PM
 * @projectName LibraryV4
 * @packageName csc150.library.models;
 */
package csc150.library.models;

import java.util.List;

public class Book {
    private String primaryIsbn;
    private String primaryKey;
    private String title;
    private String publishDate;
    private int medianPages;
    private List<String> authorNames;
    private List<String> publisher;
    private String summary;
    private String imageId;

    /**
     * Default constructor
     */
    public Book() {
        setTitle(null);
        setPublishDate(null);
        setNumberOfPages(-1);
        setAuthorNames(null);
        setPublishers(null);
        setPrimaryIsbn(null);
        setPrimaryKey(null);
        setImageId(null);
    }

    /**
     * Constructor that assigns all fields
     * @param title title
     * @param publishDate date as string
     * @param medianPages average number of pages
     * @param authorNames list of names of authors
     * @param publisher list of publishers
     * @param isbn ISBN as a string
     * @param primaryKey primary edition of the book as a OpenLibrary ID
     * @param imageId the OpenLibrary ID for the cover image
     */
    public Book(String title, String publishDate, int medianPages, List<String> authorNames,
                List<String> publisher, String isbn, String primaryKey, String imageId) {
        setPrimaryIsbn(isbn);
        setTitle(title);
        setPublishDate(publishDate);
        setNumberOfPages(medianPages);
        setAuthorNames(authorNames);
        setPublishers(publisher);
        setPrimaryKey(primaryKey);
        setImageId(imageId);
    }

    /**
     * Return the primary ISBN if it isn't null
     * @return ISBN or empty string
     */
    public String getPrimaryIsbn() {
        if (primaryIsbn == null) return "";
        return primaryIsbn + "";
    }

    /**
     * Sets the primaryISBN
     * @param primaryIsbn the ISBN to set
     */
    public void setPrimaryIsbn(String primaryIsbn) {
        this.primaryIsbn = primaryIsbn;
    }

    /**
     * Returns the title of the primary edition of the book
     * @return title as a string
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title of the primary edition of the book
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Returns publish date if it isn't null
     * @return publish date or empty string
     */
    public String getPublishDate() {
        if (publishDate == null) return "";
        return publishDate + "";
    }

    /**
     * Sets publish date
     * @param publishDate the publish date to be set
     */
    public void setPublishDate(String publishDate) {
        this.publishDate = publishDate;
    }

    /**
     * gets the number of pages in the primary edition of the book if it is set
     * @return the number of pages as a string or an empty string
     */
    public String getNumberOfPages() {
        if (medianPages == -1) return "";
        return medianPages + "";
    }

    /**
     * Set the number of pages in the primary edition of the book
     * @param numberOfPages the numberOfPages to be set
     */
    public void setNumberOfPages(int numberOfPages) {
        this.medianPages = numberOfPages;
    }

    /**
     * gets a list of authors, or null if not set
     * @return the list of authors or null
     */
    public List<String> getAuthorNames() {
        if (authorNames == null || authorNames.isEmpty()) return null;
        return authorNames;
    }

    /**
     * Set the authors as a list of strings
     * @param authorNames the list of authors to be set
     */
    public void setAuthorNames(List<String> authorNames) {
        this.authorNames = authorNames;
    }

    /**
     * gets a list of publishers, or null if not set
     * @return the list of publishers or null
     */
    public List<String> getPublisher() {
        if (publisher == null || publisher.isEmpty()) return null;
        return publisher;
    }

    /**
     * Set the publishers as a list of strings
     * @param publisher the list of publishers to be set
     */
    public void setPublishers(List<String> publisher) {
        this.publisher = publisher;
    }

    /**
     * gets the key of the primary edition of the book
     * @return the OpenLibrary ID of the primary edition of the book
     */
    public String getPrimaryKey() {
        return primaryKey;
    }

    /**
     * Sets the key of the primary edition of the book
     * @param primaryKey the OpenLibrary ID of the primary edition of the book
     */
    public void setPrimaryKey(String primaryKey) {
        this.primaryKey = primaryKey;
    }

    /**
     * gets the OpenLibrary ID for a cover image
     * @return the OpenLibrary ID for the cover image of the primary edition of the book
     */
    public String getImageId() {
        return imageId;
    }

    /**
     * Sets the OpenLibrary ID for a cover image
     * @param imageId the OpenLibrary ID for the cover image of the primary edition of the book
     */
    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    /**
     * gets the summary for the book from the primary edition
     * @return a summary of the book
     */
    public String getSummary() {
        return summary;
    }

    /**
     * Sets the summary for the book from the primary edition
     * @param summary the summary of the book
     */
    public void setSummary(String summary) {
        this.summary = summary;
    }

    /**
     * default toString method from IntelliJ
     * @return String with fields and values for this object
     */
    @Override
    public String toString() {
        return "Book{" +
                "title='" + title + '\'' +
                ", ISBNEnglish=" + primaryIsbn +
                ", firstPublishYear=" + publishDate +
                ", medianPages=" + medianPages +
                ", authorName='" + authorNames + '\'' +
                ", publisher=" + publisher +
                '}';
    }
}
