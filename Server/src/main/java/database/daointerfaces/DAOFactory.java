package database.daointerfaces;

import java.sql.Connection;
import java.sql.SQLException;

public interface DAOFactory {
    Connection getConnection() throws SQLException;

    PublisherDAO getPublisherDAO();

    BookDAO getBookDAO();

    AuthorDAO getAuthorDAO();

    AuthorBookConnectorDAO getAuthorBookConnectorDAO();
}
