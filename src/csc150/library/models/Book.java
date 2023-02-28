/**
 * @author rshirts
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
    private String authorName;
    private List<String> subjects;

    public Book() {
        setTitle(null);
        setFirstPublishYear(-1);
        setMedianPages(-1);
        setAuthorName(null);
    }

    public Book(String title, String authorName, int firstPublishYear, int medianPages, List<String> subjects) {
        this.title = title;
        this.firstPublishYear = firstPublishYear;
        this.medianPages = medianPages;
        this.authorName = authorName;
        this.subjects = subjects;
    }

    public Book(String title, String authorName, int firstPublishYear, int medianPages) {
        this.title = title;
        this.firstPublishYear = firstPublishYear;
        this.medianPages = medianPages;
        this.authorName = authorName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getFirstPublishYear() {
        return firstPublishYear;
    }

    public void setFirstPublishYear(int firstPublishYear) {
        this.firstPublishYear = firstPublishYear;
    }

    public int getMedianPages() {
        return medianPages;
    }

    public void setMedianPages(int medianPages) {
        this.medianPages = medianPages;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    @Override
    public String toString() {
        return "Book{" +
                "title='" + title + '\'' +
                ", firstPublishYear=" + firstPublishYear +
                ", medianPages=" + medianPages +
                ", authorName='" + authorName + '\'' +
                ", subjects=" + subjects +
                '}';
    }
}
