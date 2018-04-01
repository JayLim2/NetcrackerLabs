package database.service;

import entity.Book;

import java.util.List;
import org.springframework.stereotype.Service;


public interface BookService {


    Book addBook(Book book);

    void delete(Book book);

    Book getByName(String name);
    
    Book getByID(int id);

    Book editBook(Book book);

    List<Book> getAll();


}
