package Controllers;

import Models.Author;
import Models.Book;

public class BookController  {
    private Author author;

    public BookController(Author author) {
        this.author = author;
    }
  
    public BookController() {    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public void addBook(Book book){
        author.addBook(book);
    }
}