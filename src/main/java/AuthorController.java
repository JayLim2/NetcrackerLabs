import java.util.ArrayList;
import java.util.List;

class Author {
    private String name;
    private List<Book> books;
    
    public Author(String name){
        setName(name);
        books = new ArrayList<Book>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }
}

interface AuthorView {
    void printAuthor(Author author);
}

class ConsoleAuthorView implements AuthorView {
    @Override
    public void printAuthor(Author author) {
        System.out.println("Name: " + author.getName());
    }
}

public class AuthorController {
    AuthorView view;
    Author model;

    public AuthorController(Author model, AuthorView view){
        this.model = model;
        this.view = view;
    }
    
    public void updateView(){
        view.printAuthor(model);
    }
    
    public Author getAuthor(){
        return model;
    }
    
    public void execute() {
        Author author = new Author("def name");
        view.printAuthor(author);
    }

    public void addBook(Book book) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}


