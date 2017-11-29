package Models;

import java.util.LinkedList;
import java.util.List;
import javax.xml.bind.annotation.*;

@XmlRootElement
@XmlSeeAlso(Book.class)
public class Author {
    private String name;
    private List<Book> books;
    
    private static int  cid = 0;
    private int id = 0;

    {
        id = cid++;
    }

    public Author(){books = new LinkedList<Book>();}
    
    public Author(String name) {
        this.name = name;
        books = new LinkedList<Book>();
    }

    @XmlElement
    public String getName() {
        return name;
    }
    
    @XmlTransient
    public int getId(){
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    @XmlElement(name = "booksList")
    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }

    public void addBook(Book book){
        books.add(book);
        books.sort(new BooksComparator());
    }

}
