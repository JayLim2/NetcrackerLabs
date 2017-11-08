import java.util.List;

class Author {
    private String name;
    private List<Book> books;

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

interface AuthorModelLayer {
    Author getAuthor();
}

class DatabaseAuthorLayer implements AuthorModelLayer {
    @Override
    public Author getAuthor() {
        return new Author();
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
    AuthorModelLayer layer = new DatabaseAuthorLayer();
    AuthorView view = new ConsoleAuthorView();

    public void execute() {
        Author author = layer.getAuthor();
        view.printAuthor(author);
    }
}

