package database;

import database.daointerfaces.DAOFactory;
import database.postgresql.PostgreSQLAuthorDAO;
import database.postgresql.PostgreSQLBookDAO;
import database.postgresql.PostgreSQLDAOFactory;
import database.postgresql.PostgreSQLPublisherDAO;
import model.Author;
import model.Book;
import model.Publisher;

import java.sql.Connection;
import java.sql.SQLException;

public class TestDB {
    public static void main(String[] args) {
        try {
            PostgreSQLDAOFactory daoFactory = PostgreSQLDAOFactory.getInstance("Server/src/main/java/database/databaseStartScript.sql");
            PostgreSQLAuthorDAO postgreSQLAuthorDAO = daoFactory.getAuthorDAO();
            PostgreSQLBookDAO postgreSQLBookDAO = daoFactory.getBookDAO();
            PostgreSQLPublisherDAO postgreSQLPublisherDAO = daoFactory.getPublisherDAO();


//            postgreSQLAuthorDAO.delete(0);
//            System.out.println(postgreSQLAuthorDAO.create("11Esenin221"));
//            Author a = postgreSQLAuthorDAO.read(1);
//            a.setAuthorName("Esenin");
//            postgreSQLAuthorDAO.update(a);
//            System.out.println(postgreSQLAuthorDAO.read(1));

//            System.out.println(postgreSQLPublisherDAO.create("puvlic"));
//            Publisher b = postgreSQLPublisherDAO.read(0);
//            b.setPublisherName("Publisher");
//            postgreSQLPublisherDAO.update(b);
//            System.out.println(postgreSQLPublisherDAO.read(0));

//            System.out.println(postgreSQLBookDAO.create("book", 223,"asdf", 0));
            Book b = postgreSQLBookDAO.read(0);
            b.setBookName("BookName");
            postgreSQLBookDAO.update(b);
            System.out.println(postgreSQLBookDAO.read(0));


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
