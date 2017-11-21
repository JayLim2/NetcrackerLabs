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
    
    public void reInitAuthorsInBooks(){//если оставим эту штуку с xml наверное будет приватным и вызыватся только при десериализации
        for(Author author : authorsContainer.getAuthors()){
            for(Book book: author.getBooks()) 
                book.setAuthor(author);
        }
    }
}