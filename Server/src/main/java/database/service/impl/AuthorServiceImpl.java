package database.service.impl;

import database.repository.AuthorRepository;
import database.repository.BookRepository;
import database.service.AuthorService;
import entity.Author;
import entity.Book;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class AuthorServiceImpl implements AuthorService {

    @Autowired
    private AuthorRepository authorRepository;

    @Override
    public Author addAuthor(Author author) {
        Author savedAuthor = authorRepository.saveAndFlush(author);
        return savedAuthor;
    }

    @Override
    public void delete(Author author) {
        authorRepository.delete(author);
    }

    @Override
    public Author getByName(String name) {
        return authorRepository.findByName(name);
    }
    
    @Override
    public Author getByID(int id) {
        return authorRepository.findByID(id);
    }

    @Override
    public Author editAuthor(Author author) {
        return authorRepository.saveAndFlush(author);
    }

    @Override
    public List<Author> getAll() {
        return authorRepository.findAll();
    }
}
