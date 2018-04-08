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
    @Column(name =  "\"bookID\"")
    private int bookID;
    @Column(name =  "\"bookName\"", nullable=false, unique = true, length = 50)
    private String bookName;
    @Column(name = "\"publishYear\"")
    private int publishYear;
    @Column(name = "brief", nullable=false, length = 2500)
    private String brief;

    @ManyToOne
    @JoinColumn(name = "\"publisherID\"")
    private Publisher publisher;

    @ManyToMany(fetch = FetchType.EAGER,cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH}, mappedBy="books")
//    @JoinTable(name="\"authorBookConnector\"",
//            inverseJoinColumns = @JoinColumn(name="\"authorID\""),
//            joinColumns = @JoinColumn(name="\"bookID\"")
//    )
    private List<Author> authors;

    public List<Author> getAuthors() {
        return authors;
    }

    public void setAuthors(List<Author> authors) {
        this.authors = authors;
    }

    public Publisher getPublisher() {
        return publisher;
    }

    public void setPublisher(Publisher publisher) {
        this.publisher = publisher;
    }

    public Book() {
    }

    public Book(int bookID, String bookName, int publishYear, String brief) {
        this.bookID = bookID;
        this.bookName = bookName;
        this.publishYear = publishYear;
        this.brief = brief;
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

//    public int getPublisherID() {
//        return publisherID;
//    }
//
//    public void setPublisherID(int publisherID) {
//        this.publisherID = publisherID;
//    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(this.getClass().getSimpleName() + " (" + bookID
                + ", " + bookName + ", " + publishYear + ", " + brief + publisher.toString() +"{");
        for(Author author:authors){
            stringBuilder.append(" "+author.toString()+" ");
        }
        stringBuilder.append("} )");
        return stringBuilder.toString();
    }
}
