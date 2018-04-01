package controller;

import database.dao.postgresql.PostgreSQLAuthorDAO;
import database.dao.postgresql.PostgreSQLBookDAO;
import database.dao.postgresql.PostgreSQLDAOFactory;
import database.dao.postgresql.PostgreSQLPublisherDAO;
import entity.*;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.StringWriter;
import java.sql.SQLException;
import java.util.List;

public class ControllerW {

    private static ControllerW instance;
    private AuthorContainer authorContainer;
    private Author author;
    private PublisherContainer publisherContainer;
    private PostgreSQLDAOFactory postgreSQLDAOFactory;
    private PostgreSQLAuthorDAO authorDAO;
    private PostgreSQLBookDAO bookDAO;
    private PostgreSQLPublisherDAO publisherDAO;

    public ControllerW() {
        try {
            authorContainer = new AuthorContainer();
            author = new Author();
            publisherContainer = new PublisherContainer();
            postgreSQLDAOFactory = PostgreSQLDAOFactory.getInstance();
            authorDAO = postgreSQLDAOFactory.getAuthorDAO();
            bookDAO = postgreSQLDAOFactory.getBookDAO();
            publisherDAO = postgreSQLDAOFactory.getPublisherDAO();
            createAuthorContainer();
            createPublisherContainer();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static synchronized ControllerW getInstance() {
        if (instance == null) instance = new ControllerW();
        return instance;
    }

    //todo написать проверки вводимых данных в методах
    public void createAuthor(String authorName) throws SQLException {
        Author author = authorDAO.create(authorName);
        //authorContainer.addAuthor(author);
    }

    public Author readAuthor(int authorID) {
        return authorContainer.getAuthor(authorID);
    }

    public Author readAuthor(String authorName) throws SQLException {
        return authorDAO.read(authorName);
    }
    
    public void updateAuthor(int authorID, String authorName) throws SQLException {
        Author author = authorContainer.getAuthor(authorID);
        author.setAuthorName(authorName);
        authorDAO.update(author);
    }

    public void deleteAuthor(int authorID) throws SQLException {
        authorDAO.delete(authorID);
        authorContainer.deleteAuthor(authorID);
    }

    public void createPublisher(String publisherName) throws SQLException {
        Publisher publisher = publisherDAO.create(publisherName);
        publisherContainer.addPublisher(publisher);
    }

    public Publisher readPublisher(int publisherID) throws SQLException {
//todo возвращать сразу в стринге для методов чтения
        //Publisher publisher = publisherContainer.getPublisher(publisherID);
        return publisherDAO.read(publisherID);
    }
    
    public Publisher readPublisher(String publisherName) throws SQLException {
//todo возвращать сразу в стринге для методов чтения
        //Publisher publisher = publisherContainer.getPublisher(publisherID);
        return publisherDAO.read(publisherName);
    }

    public void updatePublisher(int publisherID, String publisherName) throws SQLException {
        Publisher publisher = publisherContainer.getPublisher(publisherID);
        publisher.setPublisherName(publisherName);
        publisherDAO.update(publisher);
    }

    public void deletePublisher(int publisherID) throws SQLException {
        publisherDAO.delete(publisherID);
        publisherContainer.deletePublisher(publisherID);
    }

    public void createBook(
                           String bookName,
                           int publishYear,
                           String brief,
                           int publisherID,
                           int[] authorIDs) throws SQLException {
        Book book = bookDAO.create(bookName, publishYear, brief, publisherID, authorIDs);
        //authorContainer.getAuthor(authorID).addBook(book);
    }

    public List<Book> getAllBooks() throws SQLException {
        return bookDAO.getAll();
    }

    public List<Author> getAllAuthors() throws SQLException {
        return authorDAO.getAll();
    }

    public Book readBook(int bookID) throws SQLException {
        return bookDAO.read(bookID);
    }

//    public void updateBook(int bookID,
//                           String bookName,
//                           int publishYear,
//                           String brief,
//                           int publisherID,
//                           int[] authorIDs) throws SQLException {
//        ///Book book = authorContainer.getAuthor(authorIDs[0]).getBook(bookID);//fix this.
//        Book book = new Book(bookID, bookName, publishYear, brief, publisherID, null);
//        bookDAO.update(book,authorIDs);
//    }

    public synchronized void deleteBook(int authorID, int bookID) throws SQLException {
        //authorContainer.getAuthor(authorID).removeBook(bookID);
        bookDAO.delete(bookID);
    }

    public void createAuthorContainer() {
        authorContainer = new AuthorContainer();
        try {
            for (Author author : authorDAO.getAll()) ;
            authorContainer.addAuthor(author);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createPublisherContainer() {
        publisherContainer = new PublisherContainer();
        try {
            for (Publisher publisher : publisherDAO.getAll())
                publisherContainer.addPublisher(publisher);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
//todo доделать после реализации AuthorBookConnector
//    public void createAuthor() {
//        author = new Author();
//        try {
//            for (Author author : authorDAO.getAll())
//                for (Book books : bookDAO.getAll())
//                    author.addBook(books);
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }


    private String marshalToString(Class className, Object object) throws JAXBException {
        StringWriter stringWriter = new StringWriter();
        JAXBContext context = JAXBContext.newInstance(className);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.marshal(object, stringWriter);
        return stringWriter.toString();
    }


}
