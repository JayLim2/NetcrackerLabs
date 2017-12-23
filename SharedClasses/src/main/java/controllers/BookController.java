package controllers;

import models.Author;
import models.Book;
import models.YearOutOfBoundsException;

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
    
    public boolean isEquvalent(Book book){
        return this.book.getTitle().equals(book.getTitle()) &&
            this.book.getPublishYear() == book.getPublishYear() &&
            this.book.getBrief().equals(book.getBrief()) &&
            this.book.getPublisher().equals(book.getPublisher());
    }
    
    public void modifyBook(Book book) throws YearOutOfBoundsException{
        this.book.setPublishYear(book.getPublishYear());//так. так то должно работать. но лучше перестарховаться.
        this.book.setAuthor(book.getAuthor());
        this.book.setBrief(book.getBrief());
        this.book.setPublisher(book.getPublisher());
        this.book.setTitle(book.getTitle());
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