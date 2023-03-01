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

    public Book(String title, String publishDate, int medianPages, List<String> authorNames,
                List<String> subjects, List<String> publisher, String isbn, String primaryKey, String imageId) {
        setPrimaryIsbn(isbn);
        setTitle(title);
        setPublishDate(publishDate);
        setNumberOfPages(medianPages);
        setAuthorNames(authorNames);
        setPublishers(publisher);
        setPrimaryKey(primaryKey);
        setImageId(imageId);
    }

    public String getPrimaryIsbn() {
        if (primaryIsbn == null) return "";
        return primaryIsbn + "";
    }

    public void setPrimaryIsbn(String primaryIsbn) {
        this.primaryIsbn = primaryIsbn;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPublishDate() {
        if (publishDate == null) return "";
        return publishDate + "";
    }

    public void setPublishDate(String publishDate) {
        this.publishDate = publishDate;
    }

    public String getMedianPages() {
        if (medianPages == -1) return "";
        return medianPages + "";
    }

    public void setNumberOfPages(int medianPages) {
        this.medianPages = medianPages;
    }

    public List<String> getAuthorNames() {
        if (authorNames == null || authorNames.isEmpty()) return null;
        return authorNames;
    }

    public void setAuthorNames(List<String> authorNames) {
        this.authorNames = authorNames;
    }

    public List<String> getPublisher() {
        if (publisher == null || publisher.isEmpty()) return null;
        return publisher;
    }

    public void setPublishers(List<String> publisher) {
        this.publisher = publisher;
    }

    public String getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(String primaryKey) {
        this.primaryKey = primaryKey;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

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
