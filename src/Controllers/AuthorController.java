package Controllers;

import Models.Author;
import Models.AuthorsContainer;
import Models.Book;

public class AuthorController {
    private AuthorsContainer authorsContainer;
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
    }
}


