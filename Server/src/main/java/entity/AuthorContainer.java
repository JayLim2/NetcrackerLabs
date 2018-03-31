package entity;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class AuthorContainer {
    private HashMap<Integer, Author> authors;

    public AuthorContainer() {
        this.authors = new HashMap<>();
    }

    public void addAuthor(Author author) {
        authors.put(author.getAuthorID(), author);
    }

    public void deleteAuthor(int id) {
        authors.remove(id);
    }

    public Author getAuthor(int id) {
        return authors.get(id);
    }

    public List<Author> getAuthors() {
        List<Author> authorsList = new LinkedList<>(authors.values());
        return Collections.unmodifiableList(authorsList);
    }
}
