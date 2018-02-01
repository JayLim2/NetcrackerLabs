package controllers;

import models.Author;
import models.Book;
import models.YearOutOfBoundsException;

/**
 * a wrapper for book model to provide functionality
 * @author Alexander
 */
public class BookController  {
    private Book book;

    /**
     * main constructor
     * @param book to be encapsulated 
     */
    public BookController(Book book) {
        this.book = book;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }
    
    
    /**
     * checks if contined book is equivalent(not equal!) to provided book
     * @param book book to compare to
     * @return true if equvalent, false if not 
     */
    public boolean isEquvalent(Book book){
        return this.book.getTitle().equals(book.getTitle()) &&
            this.book.getPublishYear() == book.getPublishYear() &&
            this.book.getBrief().equals(book.getBrief()) &&
            this.book.getPublisher().equals(book.getPublisher());
    }
    
    /**
     * modifies a book in container with values providet through the param book and param author
     * @param book new param encapsulation
     * @param author new author
     * @throws YearOutOfBoundsException if new values somehow contain a wrong year value even though it should be impossible
     */
    public void modifyBook(Book book, Author author) throws YearOutOfBoundsException{
        this.book.setPublishYear(book.getPublishYear());//так. так то должно работать. но лучше перестарховаться.
        this.book.setAuthor(author);
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