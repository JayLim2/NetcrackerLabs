package Models;

import java.util.UUID;

public class Book {
    private String title;
    private Author author;
    private int publishYear;
    private String publisher;
    private String brief;

    public static int id = 0;

    public Book() {    }

    public Book(String title, Author author, int publishYear, String publisher, String brief) {
        this.title = title;
        this.author = author;
        this.publishYear = publishYear;
        this.publisher = publisher;
        this.brief = brief;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
            this.title = title;
    }

    public Author getAuthor() {
            return author;
    }

    public void setAuthor(Author author) {
            this.author = author;
    }

    public int getPublishYear() {
            return publishYear;
    }

    public void setPublishYear(int publishYear) {
            this.publishYear = publishYear;
    }

    public String getPublisher() {
            return publisher;
    }

    public void setPublisher(String publisher) {
            this.publisher = publisher;
    }

    public String getBrief() {
            return brief;
    }

    public void setBrief(String brief) {
            this.brief = brief;
    }
}

