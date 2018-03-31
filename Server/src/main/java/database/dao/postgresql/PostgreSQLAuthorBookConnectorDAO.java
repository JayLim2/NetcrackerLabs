package database.dao.postgresql;

import database.dao.daointerfaces.AuthorBookConnectorDAO;
import entity.AuthorBookConnector;

import java.util.List;

public class PostgreSQLAuthorBookConnectorDAO implements AuthorBookConnectorDAO {

    @Override
    public AuthorBookConnector create(String authorName) {
        return null;
    }

    @Override
    public AuthorBookConnector read(int authorID, int bookID) {
        return null;
    }

    @Override
    public void update(AuthorBookConnector authorBookConnector) {

    }

    @Override
    public void delete(int authorID, int bookID) {

    }

    @Override
    public List<AuthorBookConnector> getAll() {
        return null;
    }
}
