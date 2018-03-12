package model;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class Author {
//todo написать аннотации на все классы моделей
    private int authorID;
    private String authorName;
    private HashMap<Integer, Book> books;

    public Author() {
        this.books = new HashMap<>();
    }

    public Author(int authorID, String authorName) {
        this.authorID = authorID;
        this.authorName = authorName;
        this.books = new HashMap<>();
    }

    public int getAuthorID() {
        return authorID;
    }

    public void setAuthorID(int authorID) {
        this.authorID = authorID;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public List<Book> getBooks() {
        List<Book> booksList = new LinkedList<>(books.values());
        return Collections.unmodifiableList(booksList);
    }

    public void addBook(Book book) {
        books.put(book.getBookID(), book);
    }

    public void removeBook(int id) {
        books.remove(id);
    }

    public Book getBook(int id) {
        return books.get(id);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(this.getClass().getSimpleName() + " (" + authorID
                + ", " + authorName + ", " + "{ " + books.toString() + " }" + " )");
        return stringBuilder.toString();
    }
}
