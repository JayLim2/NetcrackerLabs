package database.postgresql;

import database.daointerfaces.AuthorDAO;
import database.daointerfaces.BookDAO;
import database.daointerfaces.DAOFactory;
import database.daointerfaces.PublisherDAO;
import model.Publisher;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class PostgreSQLDAOFactory implements DAOFactory {
    private static final String DB_PATH = "jdbc:postgresql://localhost:5432/postgres";
    private static final String USER = "postgres";
    private static final String PASS = "root";
    //    private static final String DRIVER_NAME = "org.postgresql.Driver";
    private static final String START_SCRIPT_NAME = "Server/src/main/java/database/databaseStartScript.sql";
    private static PostgreSQLDAOFactory instance;
    private Connection connection;


    private PostgreSQLDAOFactory(String path) throws SQLException {
        connection = getConnection();
        executeSqlStartScript(path);
    }

    private PostgreSQLDAOFactory() throws SQLException {
        connection = getConnection();
        executeSqlStartScript(START_SCRIPT_NAME);
    }

    public static PostgreSQLDAOFactory getInstance(String path) throws SQLException {
        if (instance == null)
            instance = new PostgreSQLDAOFactory(path);
        return instance;
    }

    public static PostgreSQLDAOFactory getInstance() throws SQLException {
        if (instance == null)
            instance = new PostgreSQLDAOFactory();
        return instance;
    }

//    public void setConnection(String pathStartScript) {
//        try {
//            Class.forName(DRIVER_NAME);
//            connection = DriverManager.getConnection(DB_PATH, USER, PASS);
//        } catch (SQLException e) {
//            System.out.println("Error connection");
//        } catch (ClassNotFoundException e) {
//            System.out.println("Driver was not found");
//        }
//        executeSqlStartScript(pathStartScript);
//    }

    @Override
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_PATH, USER, PASS);
    }

    public void executeSqlStartScript(String path) {
        String delimiter = ";";
        Scanner scanner;
        try {
            scanner = new Scanner(new FileInputStream(path)).useDelimiter(delimiter);
            Statement currentStatement = null;
            while (scanner.hasNext()) {
                String rawStatement = scanner.next() + delimiter;
                try {
                    currentStatement = connection.createStatement();
                    currentStatement.execute(rawStatement);
                } catch (SQLException e) {
                    e.printStackTrace();
                } finally {
                    if (currentStatement != null) {
                        try {
                            currentStatement.close();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                    currentStatement = null;
                }
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public PostgreSQLAuthorDAO getAuthorDAO() {
        return new PostgreSQLAuthorDAO(connection);
    }

    @Override
    public PostgreSQLBookDAO getBookDAO() {
        return new PostgreSQLBookDAO(connection);
    }

    @Override
    public PostgreSQLPublisherDAO getPublisherDAO() {
        return new PostgreSQLPublisherDAO(connection);
    }
}

