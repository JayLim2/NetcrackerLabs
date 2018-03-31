package database.service;

import entity.Book;

import java.util.List;

public interface BookService {


    Book addBook(Book book);

    void delete(Book book);

    Book getByName(String name);

    Book editBook(Book book);

    List<Book> getAll();


}
