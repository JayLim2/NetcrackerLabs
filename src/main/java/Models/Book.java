package Models;

import javax.xml.bind.annotation.*;


@XmlRootElement
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

    @XmlElement
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
            this.title = title;
    }

    @XmlTransient
    public Author getAuthor() {
            return author;
    }

    public void setAuthor(Author author) {
            this.author = author;
    }

    @XmlElement
    public int getPublishYear() {
            return publishYear;
    }

    public void setPublishYear(int publishYear) {
            this.publishYear = publishYear;
    }

    @XmlElement
    public String getPublisher() {
            return publisher;
    }

    public void setPublisher(String publisher) {
            this.publisher = publisher;
    }

    @XmlElement
    public String getBrief() {
            return brief;
    }

    public void setBrief(String brief) {
            this.brief = brief;
    }
}

