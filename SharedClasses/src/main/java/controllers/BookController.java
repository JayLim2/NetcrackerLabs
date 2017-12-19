package controllers;

import models.Author;
import models.Book;

public class BookController  {
    private Book book;

    public BookController(Book book) {
        this.book = book;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }
    /*private Author author;

    public BookController(Author author) {
        this.author = author;
    }
  
    public BookController() {    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public void addBook(Book book){
        //author.addBook(book);

    }*/
}