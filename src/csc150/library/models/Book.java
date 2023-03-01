/**
 * @author rshirts dthompson
 * @createdOn 2/27/2023 at 1:09 PM
 * @projectName LibraryV4
 * @packageName csc150.library.models;
 */
package csc150.library.models;

import java.util.List;

public class Book {
    private String title;
    private int firstPublishYear;
    private int medianPages;
    private List<String> authorNames;
    private List<String> subjects;
    private List<String> publisher;

    /**
     * Default constructor for Book class set all values to null or -1
     */
    public Book() {
        setTitle(null);
        setFirstPublishYear(-1);
        setMedianPages(-1);
        setAuthorNames(null);
        setSubjects(null);
        setPublisher(null);
    }

    /**
     * Overloaded constructor for Book class requires the input of all parameters
     * @param title the title of the book
     * @param firstPublishYear the first year the book was published
     * @param medianPages the number of median pages
     * @param authorNames the name of the authors that wrote the book
     * @param subjects the subjects of the book
     * @param publisher who published the book
     */
    public Book(String title, int firstPublishYear, int medianPages, List<String> authorNames, List<String> subjects, List<String> publisher) {
        setTitle(title);
        setFirstPublishYear(firstPublishYear);
        setMedianPages(medianPages);
        setAuthorNames(authorNames);
        setSubjects(subjects);
        setPublisher(publisher);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFirstPublishYear() {
        if (firstPublishYear == -1) return "";
        return firstPublishYear + "";
    }

    public void setFirstPublishYear(int firstPublishYear) {
        this.firstPublishYear = firstPublishYear;
    }

    public String getMedianPages() {
        if (medianPages == -1) return "";
        return medianPages + "";
    }

    public void setMedianPages(int medianPages) {
        this.medianPages = medianPages;
    }

    public List<String> getAuthorNames() {
        if (authorNames == null || authorNames.isEmpty()) return null;
        return authorNames;
    }

    public void setAuthorNames(List<String> authorNames) {
        this.authorNames = authorNames;
    }

    public List<String> getSubjects() {
        if (subjects == null || subjects.isEmpty()) return null;
        return subjects;
    }

    public void setSubjects(List<String> subjects) {
        this.subjects = subjects;
    }

    public List<String> getPublisher() {
        if (publisher == null || publisher.isEmpty()) return null;
        return publisher;
    }

    public void setPublisher(List<String> publisher) {
        this.publisher = publisher;
    }

    @Override
    public String toString() {
        return "Book{" +
                "title='" + title + '\'' +
                ", firstPublishYear=" + firstPublishYear +
                ", medianPages=" + medianPages +
                ", authorName='" + authorNames + '\'' +
                ", subjects=" + subjects +
                ", publisher=" + publisher +
                '}';
    }
}
