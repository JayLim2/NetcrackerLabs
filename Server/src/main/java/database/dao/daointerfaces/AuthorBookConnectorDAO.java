package database.dao.daointerfaces;

import entity.AuthorBookConnector;

import java.sql.SQLException;
import java.util.List;

public interface AuthorBookConnectorDAO {
    AuthorBookConnector create(String authorName) throws SQLException;

    AuthorBookConnector read(int authorID, int bookID) throws SQLException;

    void update(AuthorBookConnector authorBookConnector) throws SQLException;

    void delete(int authorID, int bookID) throws SQLException;

    List<AuthorBookConnector> getAll() throws SQLException;
}
