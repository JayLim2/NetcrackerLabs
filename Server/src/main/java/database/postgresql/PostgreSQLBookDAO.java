package database.postgresql;

import database.daointerfaces.BookDAO;
import models.Book;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class PostgreSQLBookDAO implements BookDAO {
    private final Connection connection;

    public PostgreSQLBookDAO(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Book create(int bookID, String bookName, int publishYear, String brief, int publisherID) {
        return null;
    }

    @Override
    public Book read(int bookID) {
        return null;
    }

    @Override
    public void update(Book book) {

    }

    @Override
    public void delete(int bookID) {

    }

    @Override
    public List<Book> getAll() {
        return null;
    }
}
