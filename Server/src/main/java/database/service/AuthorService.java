package database.service;

import entity.Author;

import java.util.List;
import org.springframework.stereotype.Service;


public interface AuthorService {

    Author addAuthor(Author author);

    void delete(Author author);

    Author getByName(String name);
    
    Author getByID(int id);

    Author editAuthor(Author name);

    List<Author> getAll();
}
