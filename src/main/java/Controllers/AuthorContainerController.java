package Controllers;

import Models.Author;
import Models.AuthorsContainer;
import Models.Book;

public class AuthorContainerController {
    private AuthorsContainer authorsContainer;

    public AuthorContainerController(AuthorsContainer authorsContainer) {
        this.authorsContainer = authorsContainer;
    }

    public void addAuthor(Author author){
        authorsContainer.getAuthors().add(author);
    }
    
    public AuthorsContainer getAuthorsContainer(){
        return authorsContainer;
    }

    public void addBook(Book book, int id){
        authorsContainer.getAuthor(id).addBook(book);
    }
}