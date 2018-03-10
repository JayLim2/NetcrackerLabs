package database;

import database.daointerfaces.DAOFactory;
import database.postgresql.PostgreSQLAuthorDAO;
import database.postgresql.PostgreSQLBookDAO;
import database.postgresql.PostgreSQLDAOFactory;
import models.Author;
import models.Book;
import models.YearOutOfBoundsException;

import java.sql.Connection;
import java.sql.SQLException;

public class TestDB {
    public static void main(String[] args) {
        try {
            DAOFactory daoFactory = PostgreSQLDAOFactory.getInstance("Server/src/main/java/database/databaseStartScript.sql");
            Connection connection = daoFactory.getConnection();
            PostgreSQLAuthorDAO postgreSQLAuthorDAO = new PostgreSQLAuthorDAO(connection);
            PostgreSQLBookDAO postgreSQLBookDAO = new PostgreSQLBookDAO(connection);

/*          Tests:
            перед тем, как пробовать действия с книгами, занесите в таблицы бд любого паблишера
*/


//           create author

//            postgreSQLAuthorDAO.create("11Esenin221");


//           read author

//            System.out.println(postgreSQLAuthorDAO.read(0));

//            create book
//            try {
//                postgreSQLBookDAO.create("deleteme", 1234, "2", 0);
//            } catch (YearOutOfBoundsException e) {
//                e.printStackTrace();
//            }


//           read book
            try {
                System.out.println(postgreSQLBookDAO.read(1));
            } catch (YearOutOfBoundsException e) {
                e.printStackTrace();
            }


//            удаление книги
//          postgreSQLBookDAO.delete(4);


//            удаление автора
//            postgreSQLAuthorDAO.delete(10);


//            update Autor, пока работает некорректно, т.к. у автора старая модель со старым id

//            Author a = postgreSQLAuthorDAO.read(1);
//            a.setName("eseninTRUE");
//            postgreSQLAuthorDAO.update(a);


//          update Book, пока работает некорректно, т.к. у автора старая модель со старым id

//            Book b = postgreSQLBookDAO.read(1);
//            b.setTitle("Newtitle");
//            postgreSQLBookDAO.update(b);



        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
