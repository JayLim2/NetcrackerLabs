package database;

import database.service.PublisherService;
import database.service.impl.PublisherServiceImpl;
import entity.Publisher;

import java.sql.SQLException;
@Deprecated
public class TestDB {
    public static void main(String[] args) {
//        try {
//            PostgreSQLDAOFactory daoFactory = PostgreSQLDAOFactory.getInstance();
//            //daoFactory.executeSqlStartScript("C:\\Users\\Алескандр\\Documents\\GitHub\\NetLabsStage2\\NetcrackerLabs\\Server\\src\\main\\java\\database\\cleanupScript.sql");
//            PostgreSQLAuthorDAO postgreSQLAuthorDAO = daoFactory.getAuthorDAO();
//            PostgreSQLBookDAO postgreSQLBookDAO = daoFactory.getBookDAO();
//            PostgreSQLPublisherDAO postgreSQLPublisherDAO = daoFactory.getPublisherDAO();
//
//            System.out.println(postgreSQLAuthorDAO.create("151Esenin22381_eeee"));
//            Author a = postgreSQLAuthorDAO.read(0);
//            System.out.println(postgreSQLAuthorDAO.create("PUUUUSHHHHKINNN"));
//            a = postgreSQLAuthorDAO.read(1);
//            System.out.println(postgreSQLAuthorDAO.create("ExtraPUUUUSHHHHKINNN"));
//            a = postgreSQLAuthorDAO.read(2);
//            System.out.println(postgreSQLAuthorDAO.create("Tolstoy"));
//            a = postgreSQLAuthorDAO.read(3);
//            a.setAuthorName("Esenin");
//            postgreSQLAuthorDAO.update(a);
//            System.out.println(postgreSQLAuthorDAO.read(0));
//
//            System.out.println(postgreSQLPublisherDAO.create("publis66herrrr"));
//            Publisher p = postgreSQLPublisherDAO.read(0);
//            System.out.println(postgreSQLPublisherDAO.create("publishoi"));
//            p = postgreSQLPublisherDAO.read(1);
//            p.setPublisherName("newPublisher");
//            postgreSQLPublisherDAO.update(p);
//            System.out.println(postgreSQLPublisherDAO.read(0));
//
//            postgreSQLBookDAO.delete(1);
//            Book bb = postgreSQLBookDAO.create("book666666696", 223,"asdf", 0, new int[]{0,1});
//            System.out.println(bb);
//            Book b = postgreSQLBookDAO.read(bb.getBookID());
//            b.setBookName("BookName661000");
//            b.setBrief("newBrief7");
//            postgreSQLBookDAO.update(b, new int[] {2,3});
//            System.out.println(postgreSQLBookDAO.read(b.getBookID()));
//
//
//



        Publisher publisher = new Publisher();
        publisher.setPublisherName("OMAGAD");
            PublisherServiceImpl publisherService = new PublisherServiceImpl();
            publisherService.addPublisher(publisher);
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }


    }
}
