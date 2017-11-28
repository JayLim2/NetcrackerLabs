package Models;

import javax.xml.bind.annotation.*;
import java.util.Calendar;


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
        if (publishYear>=0&&publishYear<Calendar.getInstance().get(Calendar.YEAR)){
            try {
                this.publishYear = publishYear;
            } catch (NumberFormatException ex) {
                System.out.println("The year of publishing must be a number. Operation canceled.");
            }
        }
        else
            System.out.println("The year of publishing must be in range 0 - Current year");
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

