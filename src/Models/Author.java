package Models;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class Author {
    private String name;
    private List<Book> books;
    public static int id = 0;


    public Author() {
        books = new LinkedList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }


    public void addBook(Book book){
        book.id = books.size();
        books.add(book);
    }



}
