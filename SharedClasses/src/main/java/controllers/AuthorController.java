package controllers;

import models.Author;
import models.Book;

/**
 * Not used anywhere.
 * @author Alexander
 */
public class AuthorController {
    /**
     * Encapsulated Author model.
     */
    private Author author;

    /**
     * Not used for now.
     */
    public AuthorController() {
        author = null; //pointless
    }

    /**
     * Default constructor.
     * @param newAuthor model to wrap
     */
    public AuthorController(final Author newAuthor) {
        this.author = newAuthor;
    }

    /**
     * Gets the stored author.
     * @return the stored author
     */
    public final Author getAuthor() {
        return author;
    }

    /**
     * Sets the author to store.
     * @param newAuthor suthor to store
     */
    public final void setAuthor(final Author newAuthor) {
        this.author = newAuthor;
    }

    /**
     * @deprecated
     * @param index position in the underlying list
     * @return book at the index position in list
     */
    public final Book getBook(final int index) {
        if (index < 0 || index > author.getBooks().size()) {
            return null;
        }
        return author.getBooks().get(index);
    }

    /**
     * @param book the book to add
     * @deprecated
     * @param index book's position in the list
     */
    public final void addBook(final int index, final Book book) {
        if (index < 0 || index > author.getBooks().size()) {
            return;
        }
        author.getBooks().add(index, book);
    }

    /**
     * @param book book to set
     * @deprecated
     * @param index book's position in the list
     */
    public final void editBook(final int index, final Book book) {
        if (index < 0 || index >= author.getBooks().size()) {
            return;
        }
        author.getBooks().set(index, book);
    }

    /**
     * @deprecated
     * @param index position in the underlying list
     */
    public final void deleteBook(final int index) {
        if (index < 0 || index > author.getBooks().size()) {
            return;
        }
        author.getBooks().remove(index);
    }
}
