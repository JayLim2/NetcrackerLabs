package controllers;

import models.Author;
import models.Book;
import models.YearOutOfBoundsException;

/**
 * A wrapper for book model to provide functionality.
 * @author Alexander
 */
public class BookController  {
    /**
     * Encapsulated Book model.
     */
    private Book book;

    /**
     * Main constructor.
     * @param newBook to be encapsulated
     */
    public BookController(final Book newBook) {
        this.book = newBook;
    }

    /**
     * Gets the underlying book.
     * @return the underlying book
     */
    public final Book getBook() {
        return book;
    }

    /**
     * Sets the underlying book.
     * @param newBook the book to set
     */
    public final void setBook(final Book newBook) {
        this.book = newBook;
    }

    /**
     * Checks if contined book is equivalent(not equal!) to provided book.
     * @param newBook book to compare to
     * @return true if equvalent, false if not
     */
    public final boolean isEquvalent(final Book newBook) {
        return this.book.getTitle().equals(newBook.getTitle())
                && this.book.getPublishYear() == newBook.getPublishYear()
                && this.book.getBrief().equals(newBook.getBrief())
                && this.book.getPublisher().equals(newBook.getPublisher());
    }

    /**
     * Modifies a book in container with values
     * provided through the param book and param author.
     * @param newBook new param encapsulation
     * @param author new author
     * @throws YearOutOfBoundsException if new values
     * somehow contain a wrong year value even
     * though it should be impossible
     */
    public final void modifyBook(final Book newBook, final Author author)
            throws YearOutOfBoundsException {
        this.book.setPublishYear(newBook.getPublishYear());
        this.book.setAuthor(author);
        this.book.setBrief(newBook.getBrief());
        this.book.setPublisher(newBook.getPublisher());
        this.book.setTitle(newBook.getTitle());
    }
}
