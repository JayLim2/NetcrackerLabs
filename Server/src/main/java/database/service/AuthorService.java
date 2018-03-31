package database.service;

import entity.Author;

import java.util.List;

public interface AuthorService {

    Author addAuthor(Author author);

    void delete(Author author);

    Author getByName(String name);

    Author editAuthor(Author name);

    List<Author> getAll();
}
