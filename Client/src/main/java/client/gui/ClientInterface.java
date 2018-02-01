package client.gui;

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
     * @throws JAXBException
     * @throws XMLStreamException
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
            System.out.println("ERROR: command VIEW BOOKS can not be executed.\n");
        }

        //Если всё ок
        if (response instanceof ViewBooksResponsePacket) {
            ViewBooksResponsePacket viewBooksResponsePacket = (ViewBooksResponsePacket) response;

            return viewBooksResponsePacket.getAuthorsContainer();
            /*AuthorsContainer authorsContainer = viewBooksResponsePacket.getAuthorsContainer();
            List<Author> authors = authorsContainer.getAuthors();
            System.out.printf("%4s %30s %5s %15s %25s%n", "#", "Title", "Year", "Publisher", "Brief");
            for (Author author : authors) {
                List<Book> books = author.getBooks();
                for (Book book : books) {
                    System.out.printf("%4d %30s %5d %15s %25s%n", book.getId(), book.getTitle(), book.getPublishYear(), book.getPublisher(), book.getBrief());
                }
            }*/
        }

        return null;
    }

    /**
     * Sending the ADD_BOOK [DATA] command from the client
     *
     * @param book
     * @param author
     * @throws JAXBException
     * @throws XMLStreamException
     */
    public boolean addBook(Book book, Author author) throws JAXBException, XMLStreamException {
        //FIXME 28.12.17
        /* Проблема заключается в том, что пакет пересылается с неким номером, который
        сервером магическим образом интерпретируется как указатель на автора. �? этот НОМЕР
        вообще говоря НЕ совпадает с айдишником автора.
         */
        AddBookPacket currentCommand = new AddBookPacket(Commands.ADD_BOOK, author.getId(), book);

        contextResponsePacket = JAXBContext.newInstance(ResponsePacket.class, OkPacket.class, ErrorPacket.class);
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
            System.out.println("ERROR: command ADD BOOK can not be executed.\n");
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
     *
     * @param id
     * @param book
     * @throws JAXBException
     * @throws XMLStreamException
     */
    public boolean editBook(int id, Book book) throws JAXBException, XMLStreamException {
        System.out.println("Index = " + id);
        System.out.println("Index author = " + book.getAuthor().getId());
        SetBookPacket currentCommand = new SetBookPacket(Commands.SET_BOOK, book.getAuthor().getId(), id, book);

        System.out.println("Request sent.  0000");
        contextResponsePacket = JAXBContext.newInstance(ResponsePacket.class, OkPacket.class, ErrorPacket.class);
        unmarshResponsePacket = contextResponsePacket.createUnmarshaller();

        System.out.println("Request sent. 1111");
        commandMarshaller.marshal(currentCommand, out);

        System.out.println("Request sent. 2222");
        xer = xmi.createXMLEventReader(in);
        xer.nextEvent();
        xer.peek();

        System.out.println("Request sent. EEEEEEEEEEE");
        Book.resetId();
        Author.resetId();

        response = (ResponsePacket) unmarshResponsePacket.unmarshal(xer);
        System.out.println("Response accepted.\n");

        //Если произошла ошибка при выполнении команды
        if (response instanceof ErrorPacket) {
            System.out.println("ERROR: command SET_BOOK can not be executed.\n");
            System.out.println(((ErrorPacket) response).getDescription());
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
     *
     * @param id
     * @throws JAXBException
     * @throws XMLStreamException
     */
    public boolean deleteBook(int id) throws JAXBException, XMLStreamException {
        RemoveBookPacket currentCommand = new RemoveBookPacket(Commands.REMOVE_BOOK, id);

        contextResponsePacket = JAXBContext.newInstance(ResponsePacket.class, OkPacket.class, ErrorPacket.class);
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
            System.out.println("ERROR: command REMOVE BOOK can not be executed.\n");
            System.out.println(((ErrorPacket) response).getDescription());

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
     *
     * @throws JAXBException
     * @throws XMLStreamException
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
            System.out.println("ERROR: command VIEW AUTHORS can not be executed.\n");
        }

        //Если всё ок
        if (response instanceof ViewBooksResponsePacket) {
            ViewBooksResponsePacket viewBooksResponsePacket = (ViewBooksResponsePacket) response;

            return viewBooksResponsePacket.getAuthorsContainer();
            /*AuthorsContainer authorsContainer = viewBooksResponsePacket.getAuthorsContainer();
            List<Author> authors = authorsContainer.getAuthors();
            System.out.printf("%4s %15s %15s%n", "#", "Author name", "Count of books");
            for (Author author : authors) {
                System.out.printf("%4d %15s %15d%n", author.getId(), author.getName(), author.getBooks().size());
            }*/
        }

        return null;
    }

    /**
     * Sending the ADD_AUTHOR [DATA] command from the client
     *
     * @param authorName
     * @throws JAXBException
     * @throws XMLStreamException
     */
    public boolean addAuthor(String authorName) throws JAXBException, XMLStreamException {
        Author author = new Author(authorName);
        AddAuthorPacket currentCommand = new AddAuthorPacket(Commands.ADD_AUTHOR, author);

        contextResponsePacket = JAXBContext.newInstance(ResponsePacket.class, OkPacket.class, ErrorPacket.class);
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
            System.out.println("ERROR: command ADD_AUTHOR can not be executed.\n");
            System.out.println(((ErrorPacket) response).getDescription());
            new Alert(Alert.AlertType.ERROR, ((ErrorPacket) response).getDescription()).show();

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
     *
     * @param id
     * @param authorName
     * @throws JAXBException
     * @throws XMLStreamException
     */
    public boolean editAuthor(int id, String authorName) throws JAXBException, XMLStreamException {
        Author author = new Author(authorName);
        SetAuthorPacket currentCommand = new SetAuthorPacket(Commands.SET_AUTHOR, id, author);

        contextResponsePacket = JAXBContext.newInstance(ResponsePacket.class, OkPacket.class, ErrorPacket.class);
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
            System.out.println("ERROR: command SET_AUTHOR can not be executed.\n\n");
            System.out.println(((ErrorPacket) response).getDescription());

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
     *
     * @param id
     * @throws JAXBException
     * @throws XMLStreamException
     */
    public boolean deleteAuthor(int id) throws JAXBException, XMLStreamException {
        RemoveAuthorPacket currentCommand = new RemoveAuthorPacket(Commands.REMOVE_AUTHOR, id);

        contextResponsePacket = JAXBContext.newInstance(ResponsePacket.class, OkPacket.class, ErrorPacket.class);
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
            System.out.println("ERROR: command REMOVE AUTHOR can not be executed.\n");
            System.out.println(((ErrorPacket) response).getDescription());

            return false;
        }

        //Если всё ок
        if (response instanceof OkPacket) {
            System.out.println("Author has been removed successfully\n");
        }

        return true;
    }

    public AuthorsContainer searchBook(String title, String author, String publishYear, String brief, String publisher) throws JAXBException, XMLStreamException {
        BookFilter bookFilter = new BookFilter(title, author, publishYear, brief, publisher);
        SearchPacket currentCommand = new SearchPacket(bookFilter);

        contextResponsePacket = JAXBContext.newInstance(ResponsePacket.class, OkPacket.class, ErrorPacket.class, ViewBooksResponsePacket.class);
        unmarshResponsePacket = contextResponsePacket.createUnmarshaller();

        commandMarshaller.marshal(currentCommand, out);

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
            return viewBooksResponsePacket.getAuthorsContainer();
        }
        return null;
    }
}
