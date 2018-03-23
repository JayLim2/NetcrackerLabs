package database;

import controller.ControllerW;
import database.daointerfaces.DAOFactory;
import database.postgresql.PostgreSQLAuthorDAO;
import database.postgresql.PostgreSQLBookDAO;
import database.postgresql.PostgreSQLDAOFactory;
import database.postgresql.PostgreSQLPublisherDAO;
import model.Author;
import model.Book;
import model.Publisher;
import model.PublisherContainer;

import java.sql.Connection;
import java.sql.SQLException;

public class TestDB {
    public static void main(String[] args) {
        try {

            PostgreSQLDAOFactory daoFactory = PostgreSQLDAOFactory.getInstance();
            PostgreSQLAuthorDAO postgreSQLAuthorDAO = daoFactory.getAuthorDAO();
            PostgreSQLBookDAO postgreSQLBookDAO = daoFactory.getBookDAO();
            PostgreSQLPublisherDAO postgreSQLPublisherDAO = daoFactory.getPublisherDAO();

            //System.out.println(postgreSQLAuthorDAO.create("151Esenin22381_eeee"));
//            Author a = postgreSQLAuthorDAO.read(0);
//            a.setAuthorName("Esenin");
//            postgreSQLAuthorDAO.update(a);
//            System.out.println(postgreSQLAuthorDAO.read(0));

            System.out.println(postgreSQLPublisherDAO.create("publis66herrrr"));
            Publisher p = postgreSQLPublisherDAO.read(0);
            p.setPublisherName("newPublisher");
            postgreSQLPublisherDAO.update(p);
            System.out.println(postgreSQLPublisherDAO.read(0));

            System.out.println(postgreSQLBookDAO.create("book88", 223,"asdf", 0));
            Book b = postgreSQLBookDAO.read(0);
            b.setBookName("BookName66");
            b.setBrief("newBrief7");
            postgreSQLBookDAO.update(b);
            System.out.println(postgreSQLBookDAO.read(0));

        } catch (SQLException e) {
            e.printStackTrace();
        }


    }
}
