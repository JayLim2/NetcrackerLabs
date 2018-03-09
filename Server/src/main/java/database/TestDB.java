package database;

import database.daointerfaces.DAOFactory;
import database.postgresql.PostgreSQLAuthorDAO;
import database.postgresql.PostgreSQLDAOFactory;

import java.sql.SQLException;

public class TestDB {
    public static void main(String[] args) {
        try {
            DAOFactory daoFactory = PostgreSQLDAOFactory.getInstance();
            daoFactory.getConnection();
            PostgreSQLAuthorDAO postgreSQLAuthorDAO = new PostgreSQLAuthorDAO(daoFactory.getConnection());
            postgreSQLAuthorDAO.create("Pushkin");
            postgreSQLAuthorDAO.getAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
