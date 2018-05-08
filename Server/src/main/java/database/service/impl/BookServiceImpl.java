package database.service.impl;

import database.repository.BookRepository;
import database.service.BookService;
import entity.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookServiceImpl implements BookService {

    @Autowired
    private BookRepository bookRepository;

    @Override
    public Book addBook(Book book) {
        Book savedBook = bookRepository.saveAndFlush(book);
        return savedBook;
    }

    @Override
    public void delete(Book book) {
        bookRepository.delete(book);
    }

    @Override
    public Book getByName(String name) {
        return bookRepository.findByName(name);
    }
    
    public Book getByID(int id){
        return bookRepository.findByID(id);
    }

    @Override
    public Book editBook(Book book) {
        return bookRepository.saveAndFlush(book);
    }

    @Override
    public List<Book> getAll() {
        return bookRepository.findAll();
    }
    
    @Override
    public List<Book> filterBooks(String author){
        return bookRepository.filterBooks(author);
    }
}


