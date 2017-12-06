package Controllers;

import Models.Author;
import Models.AuthorsContainer;
import Models.Book;

public class AuthorController {
    private Author author;

    public AuthorController() {
        author = null;
    }

    public AuthorController(Author author) {
        this.author = author;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public Book getBook(int index) {
        if(index < 0 || index > author.getBooks().size())
            return null;
        return author.getBooks().get(index);
    }

    public void addBook(int index, Book book) {
        if(index < 0 || index > author.getBooks().size())
            return;
        author.getBooks().add(index, book);
    }

    public void editBook(int index, Book book) {
        if(index < 0 || index >= author.getBooks().size())
            return;
        author.getBooks().set(index, book);
    }

    public void deleteBook(int index) {
        if(index < 0 || index > author.getBooks().size())
            return;
        author.getBooks().remove(index);
    }

    /*private AuthorsContainer authorsContainer;
    private BookController bookController;

    public AuthorController() {
        authorsContainer = new AuthorsContainer();
        bookController = new BookController();
    }

    public void addAuthor(Author author){
        authorsContainer.addAuthor(author);
    }

    public void addBook(Book book, int id){
        bookController.setAuthor(authorsContainer.getAuthor(id));
        bookController.addBook(book);
    }*/
}