package database.daointerfaces;

import model.Book;

import java.sql.SQLException;
import java.util.List;

public interface BookDAO {
    Book create(String bookName, int publishYear, String brief, int publisherID, int[] authorIDs) throws SQLException;

    Book read(int bookID) throws SQLException;

    void update(Book book, int[] authorIDs) throws SQLException;

    void delete(int bookID) throws SQLException;

    List<Book> getAll() throws SQLException;
}
