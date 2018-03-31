package entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "book")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "auto_increment_book")
    @SequenceGenerator(name = "auto_increment_book", sequenceName = "\"auto_increment_book\"", allocationSize = 1)
    private int bookID;
    @Column(name =  "bookName")
    private String bookName;
    @Column(name = "publishYear")
    private int publishYear;
    @Column(name = "brief")
    private String brief;
    @Column(name = "publisherID")
    private int publisherID;

    @ManyToOne
    @JoinColumn(name = "publisherID")
    private Publisher publisher;

    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "books")
    private Set<Author> authors;

    public Set<Author> getAuthors() {
        return authors;
    }

    public void setAuthors(Set<Author> authors) {
        this.authors = authors;
    }

    public Publisher getPublisher() {
        return publisher;
    }

    public void setPublisher(Publisher publisher) {
        this.publisher = publisher;
    }

    private List<String> authorNames;

    public Book() {
        authorNames = new ArrayList<String>();
    }

    public Book(int bookID, String bookName, int publishYear, String brief, int publisherID, List<String> authorNames) {
        this.bookID = bookID;
        this.bookName = bookName;
        this.publishYear = publishYear;
        this.brief = brief;
        this.publisherID = publisherID;
        this.authorNames = authorNames;
    }

    public List<String> getAuthorNames() {
        return authorNames;
    }

    public void setAuthorNames(List<String> authorNames) {
        this.authorNames = authorNames;
    }

    public int getBookID() {
        return bookID;
    }

    public void setBookID(int bookID) {
        this.bookID = bookID;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public int getPublishYear() {
        return publishYear;
    }

    public void setPublishYear(int publishYear) {
        this.publishYear = publishYear;
    }

    public String getBrief() {
        return brief;
    }

    public void setBrief(String brief) {
        this.brief = brief;
    }

    public int getPublisherID() {
        return publisherID;
    }

    public void setPublisherID(int publisherID) {
        this.publisherID = publisherID;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(this.getClass().getSimpleName() + " (" + bookID
                + ", " + bookName + ", " + publishYear + ", " + brief + "{");
        for (String aname : authorNames) {
            stringBuilder.append(aname + ", ");
        }
        stringBuilder.append("} )");
        return stringBuilder.toString();
    }
}
