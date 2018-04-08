package entity;


import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "\"publisher\"", schema = "public", catalog = "postgres")
public class Publisher {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "auto_increment_publisher")
    @SequenceGenerator(name = "auto_increment_publisher", sequenceName = "\"auto_increment_publisher\"", allocationSize = 1)
    @Column (name = "\"publisherID\"")
    private int publisherID;
    @Column (name = "\"publisherName\"",nullable=false, unique = true, length = 30)
    private String publisherName;
//    пока не понял, нужно ли объявлять @OneToMany в издателе, если объявлено ManyToOne в книгу с указанием publisher_id
//    @OneToMany (fetch = FetchType.EAGER, mappedBy = "book")
//    private List<Book> books;

    public Publisher() {
    }

    public Publisher(int publisherID, String publisherName) {
        this.publisherID = publisherID;
        this.publisherName = publisherName;
    }

//    public List<Book> getBooks() {
//        return books;
//    }
//
//    public void setBooks(List<Book> books) {
//        this.books = books;
//    }

    public int getPublisherID() {
        return publisherID;
    }

    public void setPublisherID(int publisherID) {
        this.publisherID = publisherID;
    }

    public String getPublisherName() {
        return publisherName;
    }

    public void setPublisherName(String publisherName) {
        this.publisherName = publisherName;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(this.getClass().getSimpleName() + " (" + publisherID
                + ", " + publisherName + ")");
        return stringBuilder.toString();
    }
}
