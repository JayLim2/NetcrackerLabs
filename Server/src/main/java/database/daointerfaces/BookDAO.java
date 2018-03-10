package database.daointerfaces;

import models.Book;
import models.YearOutOfBoundsException;

import java.sql.SQLException;
import java.util.List;

public interface BookDAO {
    Book create(String bookName, int publishYear, String brief, int publisherID) throws SQLException, YearOutOfBoundsException;

    Book read(int bookID) throws SQLException, YearOutOfBoundsException;

    void update(Book book)throws SQLException;

    void delete(int bookID) throws SQLException;

    List<Book> getAll() throws SQLException, YearOutOfBoundsException;


}
