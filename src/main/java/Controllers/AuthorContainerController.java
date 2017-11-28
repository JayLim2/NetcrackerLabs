package Controllers;

import Models.Author;
import Models.AuthorsContainer;
import Models.Book;

import java.util.List;

public class AuthorContainerController {
    private AuthorsContainer authorsContainer;

    public AuthorContainerController(AuthorsContainer authorsContainer) {
        this.authorsContainer = authorsContainer;
    }

    public void addAuthor(Author author){
        authorsContainer.getAuthors().add(author);
    }
    
    public Author getAuthor(int id){
        return authorsContainer.getAuthor(id);
    }
    
    public Book getBook(int id){
        if ((id < 0)||(id >= countBooks())) throw new IndexOutOfBoundsException();
        Book res = null;
        for(Author author: authorsContainer.getAuthors())
            if (id < author.getBooks().size())
            {
                res = author.getBooks().get(id);
                break;
            }
            else
                id -= author.getBooks().size();
        return res;
    }
    
    private int countBooks(){
        int size = 0;
        for(Author author:authorsContainer.getAuthors())
            size += author.getBooks().size();
        return size;
    }
    
    public void removeBook(int id){
        if ((id < 0)||(id >= countBooks())) throw new IndexOutOfBoundsException();
        for(Author author: authorsContainer.getAuthors())
            if (id < author.getBooks().size())
            {
                author.getBooks().remove(id);
                break;
            }
            else
                id -= author.getBooks().size();
    }
    
    public void removeAuthor(int id){
        authorsContainer.getAuthors().remove(id);
    }
    
    public AuthorsContainer getAuthorsContainer(){
        return authorsContainer;
    }

    public void addBook(Book book, int id){
        authorsContainer.getAuthor(id).addBook(book);
    }

    public void addBook(Book book, int id, int publishYear){
        authorsContainer.getAuthor(id).addBook(book);
    }
    
    public void reInitAuthorsInBooks(){//если оставим эту штуку с xml наверное будет приватным и вызыватся только при десериализации
        for(Author author : authorsContainer.getAuthors()){
            for(Book book: author.getBooks()) 
                book.setAuthor(author);
        }
    }
    //Проверяет есть ли у автора книги. Если их нет - вернет false
    public boolean checkBooksInAuthor(){
        List<Author> tmp =  authorsContainer.getAuthors();
        for (int i = 0; i < tmp.size(); i++) {
            if (!tmp.get(i).getBooks().isEmpty())
                return true;
        }
        return false;
    }
}