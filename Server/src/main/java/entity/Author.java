package entity;

import javax.persistence.*;
import java.util.*;

@Entity
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "auto_increment_author")
    @SequenceGenerator(name = "auto_increment_author", sequenceName = "\"auto_increment_author\"", allocationSize = 1)
    @Column (name = "\"authorID\"")
    private int authorID;
    @Column (name = "\"authorName\"")
    private String authorName;


    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH}, fetch = FetchType.EAGER)
    @JoinTable(name="\"authorBookConnector\"",
            joinColumns = @JoinColumn(name="\"authorID\""),
            inverseJoinColumns = @JoinColumn(name="\"bookID\"")
    )
    private List<Book> books;

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }

    public Author() {
//        this.books = new HashMap<>();
    }

    public Author(int authorID, String authorName) {
        this.authorID = authorID;
        this.authorName = authorName;

    }

    public int getAuthorID() {
        return authorID;
    }

    public void setAuthorID(Integer authorID) {
        this.authorID = authorID;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }



//    public void addBook(Book book) {
//        books.put(book.getBookID(), book);
//    }
//
//    public void removeBook(int id) {
//        books.remove(id);
//    }
//
//    public Book getBook(int id) {
//        return books.get(id);
//    }

    @Override
    public String toString() {//Мне лень обертку для предачи в таимлиф делать 
        StringBuilder stringBuilder = new StringBuilder();
//        stringBuilder.append(this.getClass().getSimpleName() + " (" + authorID
//                + ", " + authorName + " )");
        return authorName;//stringBuilder.toString();
    }
}
