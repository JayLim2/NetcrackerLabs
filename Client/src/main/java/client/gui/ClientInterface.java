package client.gui;

import controllers.AuthorContainerController;
import javafx.scene.control.Alert;
import models.Author;
import models.AuthorsContainer;
import models.Book;
import models.BookFilter;
import protocol.*;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Client interface class
 * <p>
 * This class communicates with the server.
 * Send commands and data to the server and receive a response from the server
 * in accordance with the established communication protocol.
 *
 * @author Sergey Komarov
 * @author Rostislav Korostelev (SEARCH operation and some fixes).
 */
public class ClientInterface {
    private Socket clientSocket;
    private OutputStream out;
    private InputStream in;
    private Marshaller commandMarshaller;
    private JAXBContext contextCommands;
    private XMLInputFactory xmi;
    private XMLEventReader xer;
    private ResponsePacket response;
    JAXBContext contextResponsePacket;
    Unmarshaller unmarshResponsePacket;

    /**
     * Constructor of an encapsulated client interface object
     *
     * @param clientSocket      socket of client
     * @param out               output stream
     * @param in                input stream
     * @param commandMarshaller command marshaller to marshall commands for server
     * @param contextCommands   context commands
     * @param xmi               XMLInputFactory
     */
    public ClientInterface(Socket clientSocket, OutputStream out, InputStream in, Marshaller commandMarshaller,
                           JAXBContext contextCommands, XMLInputFactory xmi) {
        this.clientSocket = clientSocket;
        this.out = out;
        this.in = in;
        this.commandMarshaller = commandMarshaller;
        this.contextCommands = contextCommands;
        this.xmi = xmi;
    }

    /**
     * Sending VIEW_BOOKS command from the client
     *
     * @throws JAXBException by JAXB methods
     * @throws XMLStreamException by marshaller/unmarshaller
     */
    public AuthorsContainer viewBooks() throws JAXBException, XMLStreamException {
        Book.resetId();
        Author.resetId();
        //====================== VIEW BOOKS ==============================
        ViewBooksPacket currentCommand = new ViewBooksPacket(Commands.VIEW_BOOKS);

        contextResponsePacket = JAXBContext.newInstance(ResponsePacket.class, OkPacket.class, ErrorPacket.class, ViewBooksResponsePacket.class);
        unmarshResponsePacket = contextResponsePacket.createUnmarshaller();

        commandMarshaller.marshal(currentCommand, out);

        xer = xmi.createXMLEventReader(in);
        xer.nextEvent();
        xer.peek();

        response = (ResponsePacket) unmarshResponsePacket.unmarshal(xer);
        System.out.println("Response accepted.\n");

        //Если произошла ошибка при выполнении команды
        if (response instanceof ErrorPacket) {
            new Alert(Alert.AlertType.ERROR, "Command VIEW BOOKS can not be executed.\n\n" + ((ErrorPacket) response).getDescription()).show();
        }

        //Если всё ок
        if (response instanceof ViewBooksResponsePacket) {
            ViewBooksResponsePacket viewBooksResponsePacket = (ViewBooksResponsePacket) response;

            return viewBooksResponsePacket.getAuthorsContainer();
        }

        return null;
    }

    /**
     * Sending the ADD_BOOK [DATA] command from the client
     * Encapsulates the command and data in itself.
     * @param book new book object
     * @param author book author object
     * @throws JAXBException by JAXB methods
     * @throws XMLStreamException by marshaller/unmarshaller
     */
    public boolean addBook(Book book, Author author) throws JAXBException, XMLStreamException {
        AddBookPacket currentCommand = new AddBookPacket(Commands.ADD_BOOK, author.getId(), book);

        contextResponsePacket = JAXBContext.newInstance(ResponsePacket.class, OkPacket.class, ErrorPacket.class, ViewBooksResponsePacket.class);
        unmarshResponsePacket = contextResponsePacket.createUnmarshaller();

        commandMarshaller.marshal(currentCommand, out);

        xer = xmi.createXMLEventReader(in);
        xer.nextEvent();
        xer.peek();
        Book.resetId();
        Author.resetId();

        response = (ResponsePacket) unmarshResponsePacket.unmarshal(xer);
        System.out.println("Response accepted.\n");

        //Если произошла ошибка при выполнении команды
        if (response instanceof ErrorPacket) {
            new Alert(Alert.AlertType.ERROR, "Command ADD BOOK can not be executed.\n\n" + ((ErrorPacket) response).getDescription()).show();
            return false;
        }

        //Если всё ок
        if (response instanceof OkPacket) {
            System.out.println("Book added.\n");
        }

        return true;
    }

    /**
     * Sending the SET_BOOK [DATA] command from the client by ID
     * Encapsulates the command and data in itself.
     * @param id unique ID number of book in books table
     * @param book edited book object
     * @throws JAXBException by JAXB methods
     * @throws XMLStreamException by marshaller/unmarshaller
     */
    public boolean editBook(int id, Book book) throws JAXBException, XMLStreamException {
        SetBookPacket currentCommand = new SetBookPacket(Commands.SET_BOOK, book.getAuthor().getId(), id, book);

        contextResponsePacket = JAXBContext.newInstance(ResponsePacket.class, OkPacket.class, ErrorPacket.class,ViewBooksResponsePacket.class);
        unmarshResponsePacket = contextResponsePacket.createUnmarshaller();

        commandMarshaller.marshal(currentCommand, out);

        xer = xmi.createXMLEventReader(in);
        xer.nextEvent();
        xer.peek();

        Book.resetId();
        Author.resetId();

        response = (ResponsePacket) unmarshResponsePacket.unmarshal(xer);
        System.out.println("Response accepted.\n");

        //Если произошла ошибка при выполнении команды
        if (response instanceof ErrorPacket) {
            new Alert(Alert.AlertType.ERROR, "Command SET BOOK can not be executed.\n\n" + ((ErrorPacket) response).getDescription()).show();
            return false;
        }

        //Если всё ок
        if (response instanceof OkPacket) {
            System.out.println("The book was changed successfully.\n");
        }

        return true;
    }

    /**
     * Sending the REMOVE BOOK command from the client by ID
     * Encapsulates the command and data in itself.
     * @param id unique ID number of book in books table
     * @throws JAXBException by JAXB methods
     * @throws XMLStreamException by marshaller/unmarshaller
     */
    public boolean deleteBook(int id) throws JAXBException, XMLStreamException {
        RemoveBookPacket currentCommand = new RemoveBookPacket(Commands.REMOVE_BOOK, id);

        contextResponsePacket = JAXBContext.newInstance(ResponsePacket.class, OkPacket.class, ErrorPacket.class,ViewBooksResponsePacket.class);
        unmarshResponsePacket = contextResponsePacket.createUnmarshaller();

        commandMarshaller.marshal(currentCommand, out);

        xer = xmi.createXMLEventReader(in);
        xer.nextEvent();
        xer.peek();
        Book.resetId();
        Author.resetId();

        response = (ResponsePacket) unmarshResponsePacket.unmarshal(xer);
        System.out.println("Response accepted.\n");

        //Если произошла ошибка при выполнении команды
        if (response instanceof ErrorPacket) {
            new Alert(Alert.AlertType.ERROR, "Command REMOVE BOOK can not be executed.\n\n" + ((ErrorPacket) response).getDescription()).show();
            return false;
        }

        //Если всё ок
        if (response instanceof OkPacket) {
            System.out.println("Book has been removed successfully\n");
        }

        return true;
    }

    //-------------------------------

    /**
     * Sending the VIEW_AUTHORS command from the client
     * @throws JAXBException by JAXB methods
     * @throws XMLStreamException by marshaller/unmarshaller
     */
    public AuthorsContainer viewAuthors() throws JAXBException, XMLStreamException {
        Book.resetId();
        Author.resetId();
        //пока реализована точно так же как VIEW_BOOKS
        ViewBooksPacket currentCommand = new ViewBooksPacket(Commands.VIEW_BOOKS);

        contextResponsePacket = JAXBContext.newInstance(ResponsePacket.class, OkPacket.class, ErrorPacket.class, ViewBooksResponsePacket.class);
        unmarshResponsePacket = contextResponsePacket.createUnmarshaller();

        commandMarshaller.marshal(currentCommand, out);

        xer = xmi.createXMLEventReader(in);
        xer.nextEvent();
        xer.peek();

        response = (ResponsePacket) unmarshResponsePacket.unmarshal(xer);
        System.out.println("Response accepted.\n");

        //Если произошла ошибка при выполнении команды
        if (response instanceof ErrorPacket) {
            new Alert(Alert.AlertType.ERROR, "Command VIEW AUTHORS can not be executed.\n\n" + ((ErrorPacket) response).getDescription()).show();
        }

        //Если всё ок
        if (response instanceof ViewBooksResponsePacket) {
            ViewBooksResponsePacket viewBooksResponsePacket = (ViewBooksResponsePacket) response;

            return viewBooksResponsePacket.getAuthorsContainer();
        }

        return null;
    }

    /**
     * Sending the ADD_AUTHOR [DATA] command from the client
     * Encapsulates the command and data in itself.
     * @param authorName author name
     * @throws JAXBException by JAXB methods
     * @throws XMLStreamException by marshaller/unmarshaller
     */
    public boolean addAuthor(String authorName) throws JAXBException, XMLStreamException {
        Author author = new Author(authorName);
        AddAuthorPacket currentCommand = new AddAuthorPacket(Commands.ADD_AUTHOR, author);

        contextResponsePacket = JAXBContext.newInstance(ResponsePacket.class, OkPacket.class, ErrorPacket.class,ViewBooksResponsePacket.class);
        unmarshResponsePacket = contextResponsePacket.createUnmarshaller();

        commandMarshaller.marshal(currentCommand, out);

        xer = xmi.createXMLEventReader(in);
        xer.nextEvent();
        xer.peek();
        Book.resetId();
        Author.resetId();

        response = (ResponsePacket) unmarshResponsePacket.unmarshal(xer);
        System.out.println("Response accepted.\n");

        //Если произошла ошибка при выполнении команды
        if (response instanceof ErrorPacket) {
            new Alert(Alert.AlertType.ERROR, "Command ADD AUTHOR can not be executed.\n\n" + ((ErrorPacket) response).getDescription()).show();
            return false;
        }

        //Если всё ок
        if (response instanceof OkPacket) {
            System.out.println("Author added successfully\n");
        }

        return true;
    }

    /**
     * Sending the SET_AUTHOR [NEW_DATA] command from the client by ID
     * Encapsulates the command and data in itself.
     * @param id unique ID number of author in authors table
     * @param authorName author name
     * @throws JAXBException by JAXB methods
     * @throws XMLStreamException by marshaller/unmarshaller
     */
    public boolean editAuthor(int id, String authorName) throws JAXBException, XMLStreamException {
        Author author = new Author(authorName);
        SetAuthorPacket currentCommand = new SetAuthorPacket(Commands.SET_AUTHOR, id, author);

        contextResponsePacket = JAXBContext.newInstance(ResponsePacket.class, OkPacket.class, ErrorPacket.class,ViewBooksResponsePacket.class);
        unmarshResponsePacket = contextResponsePacket.createUnmarshaller();

        commandMarshaller.marshal(currentCommand, out);

        xer = xmi.createXMLEventReader(in);
        xer.nextEvent();
        xer.peek();
        //Book.resetId();
        //Author.resetId();

        response = (ResponsePacket) unmarshResponsePacket.unmarshal(xer);
        System.out.println("Response accepted.\n");

        //Если произошла ошибка при выполнении команды
        if (response instanceof ErrorPacket) {
            new Alert(Alert.AlertType.ERROR, "Command SET AUTHOR can not be executed.\n\n" + ((ErrorPacket) response).getDescription()).show();
            return false;
        }

        //Если всё ок
        if (response instanceof OkPacket) {
            System.out.println("The author has changed successfully.\n");
        }

        return true;
    }

    /**
     * Sending the REMOVE_AUTHOR command from the client by ID
     * Encapsulates the command and data in itself.
     * @param id unique ID number of author in authors table
     * @throws JAXBException by JAXB methods
     * @throws XMLStreamException by marshaller/unmarshaller
     */
    public boolean deleteAuthor(int id) throws JAXBException, XMLStreamException {
        RemoveAuthorPacket currentCommand = new RemoveAuthorPacket(Commands.REMOVE_AUTHOR, id);

        contextResponsePacket = JAXBContext.newInstance(ResponsePacket.class, OkPacket.class, ErrorPacket.class,ViewBooksResponsePacket.class);
        unmarshResponsePacket = contextResponsePacket.createUnmarshaller();

        commandMarshaller.marshal(currentCommand, out);

        xer = xmi.createXMLEventReader(in);
        xer.nextEvent();
        xer.peek();
        //Book.resetId();
        //Author.resetId();

        response = (ResponsePacket) unmarshResponsePacket.unmarshal(xer);
        System.out.println("Response accepted.\n");

        //Если произошла ошибка при выполнении команды
        if (response instanceof ErrorPacket) {
            new Alert(Alert.AlertType.ERROR, "Command REMOVE AUTHOR can not be executed.\n\n" + ((ErrorPacket) response).getDescription()).show();

            return false;
        }

        //Если всё ок
        if (response instanceof OkPacket) {
            System.out.println("Author has been removed successfully\n");
        }

        return true;
    }

    /**
     * Sending the SEARCH BOOK command to the server
     * Encapsulates the command and data in itself.
     * @param title book title
     * @param author author of book
     * @param publishYear year when book published
     * @param brief short description of book
     * @param publisher name of book publisher
     * @return AuthorsConainer with results of request if request successful OR null if else.
     * @throws JAXBException
     * @throws XMLStreamException
     */
    public AuthorsContainer searchBook(String title, String author, String publishYear, String brief, String publisher) throws JAXBException, XMLStreamException {
        BookFilter bookFilter = new BookFilter(title, author, publishYear, brief, publisher);
        SearchPacket currentCommand = new SearchPacket(bookFilter);

        contextResponsePacket = JAXBContext.newInstance(ResponsePacket.class, OkPacket.class, ErrorPacket.class, ViewBooksResponsePacket.class);
        unmarshResponsePacket = contextResponsePacket.createUnmarshaller();

        JAXBContext contextCommands1 = JAXBContext.newInstance(CommandPacket.class, ViewBooksPacket.class, AddBookPacket.class, SetBookPacket.class, RemoveBookPacket.class, AddAuthorPacket.class, SetAuthorPacket.class, RemoveAuthorPacket.class, SearchPacket.class);
        Marshaller commandMarshaller1 = contextCommands1.createMarshaller();
        commandMarshaller1.marshal(currentCommand, out);

        xer = xmi.createXMLEventReader(in);
        xer.nextEvent();
        xer.peek();
        Book.resetId();
        Author.resetId();

        ViewBooksResponsePacket response = (ViewBooksResponsePacket) unmarshResponsePacket.unmarshal(xer);
        System.out.println("Response accepted.\n");

        //Если произошла ошибка при выполнении команды
        if (response instanceof ViewBooksResponsePacket) {
            ViewBooksResponsePacket viewBooksResponsePacket = response;
            AuthorContainerController aCCT = new AuthorContainerController(response.getAuthorsContainer());
            aCCT.reInitAuthorsInBooks();
            return viewBooksResponsePacket.getAuthorsContainer();
        }
        return null;
    }
}
