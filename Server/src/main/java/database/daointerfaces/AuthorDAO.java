package database.daointerfaces;



import models.Author;

import java.sql.SQLException;
import java.util.List;

public interface AuthorDAO {
    Author create(String authorName) throws SQLException;

    Author read(int authorID) throws SQLException;

    void update(Author book)throws SQLException;

    void delete(int authorID) throws SQLException;

    List<Author> getAll() throws SQLException;
}