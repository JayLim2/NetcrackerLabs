package client.gui;

import models.Author;
import models.AuthorsContainer;
import models.Book;
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
     * Отправка из клиента команды VIEW_BOOKS
     *
     * @throws JAXBException
     * @throws XMLStreamException
     */
    public AuthorsContainer viewBooks() throws JAXBException, XMLStreamException {
        Book.resetId();
        Author.resetId();
        //====================== VIEW BOOKS ==============================
        ViewBooksPacket currentCommand = new ViewBooksPacket(Commands.VIEW_BOOKS);

        JAXBContext contextResponsePacket = JAXBContext.newInstance(ResponsePacket.class, OkPacket.class, ErrorPacket.class, ViewBooksResponsePacket.class);
        Unmarshaller unmarshResponsePacket = contextResponsePacket.createUnmarshaller();

        commandMarshaller.marshal(currentCommand, out);

        xer = xmi.createXMLEventReader(in);
        xer.nextEvent();
        xer.peek();

        ResponsePacket response = (ResponsePacket) unmarshResponsePacket.unmarshal(xer);
        System.out.println("Response accepted.\n");

        //Если произошла ошибка при выполнении команды
        if (response instanceof ErrorPacket) {
            System.out.println("ОШИБКА: невозможно выполнить команду VIEW BOOKS.");
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
     * Отправка из клиента команды ADD_BOOK [DATA]
     *
     * @param book
     * @throws JAXBException
     * @throws XMLStreamException
     */
    public void addBook(Book book) throws JAXBException, XMLStreamException {
        AddBookPacket currentCommand = new AddBookPacket(Commands.ADD_BOOK, 0, book);

        JAXBContext contextResponsePacket = JAXBContext.newInstance(ResponsePacket.class, OkPacket.class, ErrorPacket.class);
        Unmarshaller unmarshResponsePacket = contextResponsePacket.createUnmarshaller();

        commandMarshaller.marshal(currentCommand, out);

        xer = xmi.createXMLEventReader(in);
        xer.nextEvent();
        xer.peek();
        Book.resetId();
        Author.resetId();

        ResponsePacket response = (ResponsePacket) unmarshResponsePacket.unmarshal(xer);
        System.out.println("Response accepted.\n");

        //Если произошла ошибка при выполнении команды
        if (response instanceof ErrorPacket) {
            System.out.println("ОШИБКА: невозможно выполнить команду ADD BOOK.\n");
        }

        //Если всё ок
        if (response instanceof OkPacket) {
            System.out.println("Книга добавлена успешно.\n");
        }
    }

    /**
     * Отправка из клиента команды SET_BOOK [DATA] by ID
     *
     * @param id
     * @param book
     * @throws JAXBException
     * @throws XMLStreamException
     */
    public void editBook(int id, Book book) throws JAXBException, XMLStreamException {
        SetBookPacket currentCommand = new SetBookPacket(Commands.SET_BOOK, 0, 0, book);

        JAXBContext contextResponsePacket = JAXBContext.newInstance(ResponsePacket.class, OkPacket.class, ErrorPacket.class);
        Unmarshaller unmarshResponsePacket = contextResponsePacket.createUnmarshaller();

        commandMarshaller.marshal(currentCommand, out);

        xer = xmi.createXMLEventReader(in);
        xer.nextEvent();
        xer.peek();
        Book.resetId();
        Author.resetId();

        ResponsePacket response = (ResponsePacket) unmarshResponsePacket.unmarshal(xer);
        System.out.println("Response accepted.\n");

        //Если произошла ошибка при выполнении команды
        if (response instanceof ErrorPacket) {
            System.out.println("ОШИБКА: невозможно выполнить команду EDIT BOOK.\n");
            System.out.println(((ErrorPacket) response).getDescription());
        }

        //Если всё ок
        if (response instanceof OkPacket) {
            System.out.println("Книга изменена успешно.\n");
        }
    }

    /**
     * Отправка из клиента команды REMOVE_BOOK by ID
     *
     * @param id
     * @throws JAXBException
     * @throws XMLStreamException
     */
    public void deleteBook(int id) throws JAXBException, XMLStreamException {
        RemoveBookPacket currentCommand = new RemoveBookPacket(Commands.REMOVE_BOOK, id);

        JAXBContext contextResponsePacket = JAXBContext.newInstance(ResponsePacket.class, OkPacket.class, ErrorPacket.class);
        Unmarshaller unmarshResponsePacket = contextResponsePacket.createUnmarshaller();

        commandMarshaller.marshal(currentCommand, out);

        xer = xmi.createXMLEventReader(in);
        xer.nextEvent();
        xer.peek();
        Book.resetId();
        Author.resetId();

        ResponsePacket response = (ResponsePacket) unmarshResponsePacket.unmarshal(xer);
        System.out.println("Response accepted.\n");

        //Если произошла ошибка при выполнении команды
        if (response instanceof ErrorPacket) {
            System.out.println("ОШИБКА: невозможно выполнить команду DELETE BOOK.\n");
            System.out.println(((ErrorPacket) response).getDescription());
        }

        //Если всё ок
        if (response instanceof OkPacket) {
            System.out.println("Книга удалена успешно.\n");
        }
    }

    //-------------------------------

    /**
     * FIXME 25.12.17
     * Отправка из клиента команды VIEW_AUTHORS
     *
     * @throws JAXBException
     * @throws XMLStreamException
     */
    public AuthorsContainer viewAuthors() throws JAXBException, XMLStreamException {
        Book.resetId();
        Author.resetId();
        //пока реализована точно так же как VIEW_BOOKS
        ViewBooksPacket currentCommand = new ViewBooksPacket(Commands.VIEW_BOOKS);

        JAXBContext contextResponsePacket = JAXBContext.newInstance(ResponsePacket.class, OkPacket.class, ErrorPacket.class, ViewBooksResponsePacket.class);
        Unmarshaller unmarshResponsePacket = contextResponsePacket.createUnmarshaller();

        commandMarshaller.marshal(currentCommand, out);

        xer = xmi.createXMLEventReader(in);
        xer.nextEvent();
        xer.peek();

        ResponsePacket response = (ResponsePacket) unmarshResponsePacket.unmarshal(xer);
        System.out.println("Response accepted.\n");

        //Если произошла ошибка при выполнении команды
        if (response instanceof ErrorPacket) {
            System.out.println("ОШИБКА: невозможно выполнить команду VIEW BOOKS.");
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
     * Отправка из клиента команды ADD_AUTHOR [DATA]
     *
     * @param authorName
     * @throws JAXBException
     * @throws XMLStreamException
     */
    public void addAuthor(String authorName) throws JAXBException, XMLStreamException {

        System.out.println("111AAA?");
        Author author = new Author(authorName);
        AddAuthorPacket currentCommand = new AddAuthorPacket(Commands.ADD_AUTHOR, author);

        JAXBContext contextResponsePacket = JAXBContext.newInstance(ResponsePacket.class, OkPacket.class, ErrorPacket.class);
        Unmarshaller unmarshResponsePacket = contextResponsePacket.createUnmarshaller();

        commandMarshaller.marshal(currentCommand, out);

        xer = xmi.createXMLEventReader(in);
        xer.nextEvent();
        xer.peek();
        Book.resetId();
        Author.resetId();

        ResponsePacket response = (ResponsePacket) unmarshResponsePacket.unmarshal(xer);
        System.out.println("Response accepted.\n");

        //Если произошла ошибка при выполнении команды
        if (response instanceof ErrorPacket) {
            System.out.println("ОШИБКА: невозможно выполнить команду ADD AUTHOR.\n");
            System.out.println(((ErrorPacket) response).getDescription());
        }

        //Если всё ок
        if (response instanceof OkPacket) {
            System.out.println("Автор добавлен успешно.\n");
        }
    }

    /**
     * Отправка из клиента команды SET_AUTHOR [NEW_DATA] by ID
     *
     * @param id
     * @param authorName
     * @throws JAXBException
     * @throws XMLStreamException
     */
    public void editAuthor(int id, String authorName) throws JAXBException, XMLStreamException {
        Author author = new Author(authorName);
        SetAuthorPacket currentCommand = new SetAuthorPacket(Commands.SET_AUTHOR, id, author);

        JAXBContext contextResponsePacket = JAXBContext.newInstance(ResponsePacket.class, OkPacket.class, ErrorPacket.class);
        Unmarshaller unmarshResponsePacket = contextResponsePacket.createUnmarshaller();

        commandMarshaller.marshal(currentCommand, out);

        xer = xmi.createXMLEventReader(in);
        xer.nextEvent();
        xer.peek();
        //Book.resetId();
        //Author.resetId();

        ResponsePacket response = (ResponsePacket) unmarshResponsePacket.unmarshal(xer);
        System.out.println("Response accepted.\n");

        //Если произошла ошибка при выполнении команды
        if (response instanceof ErrorPacket) {
            System.out.println("ОШИБКА: невозможно выполнить команду SET AUTHOR.\n");
            System.out.println(((ErrorPacket) response).getDescription());
        }

        //Если всё ок
        if (response instanceof OkPacket) {
            System.out.println("Автор изменен успешно.\n");
        }
    }

    /**
     * Отправка из клиента команды REMOVE_AUTHOR by ID
     *
     * @param id
     * @throws JAXBException
     * @throws XMLStreamException
     */
    public void deleteAuthor(int id) throws JAXBException, XMLStreamException {
        RemoveAuthorPacket currentCommand = new RemoveAuthorPacket(Commands.REMOVE_AUTHOR, id);

        JAXBContext contextResponsePacket = JAXBContext.newInstance(ResponsePacket.class, OkPacket.class, ErrorPacket.class);
        Unmarshaller unmarshResponsePacket = contextResponsePacket.createUnmarshaller();

        commandMarshaller.marshal(currentCommand, out);

        xer = xmi.createXMLEventReader(in);
        xer.nextEvent();
        xer.peek();
        //Book.resetId();
        //Author.resetId();

        ResponsePacket response = (ResponsePacket) unmarshResponsePacket.unmarshal(xer);
        System.out.println("Response accepted.\n");

        //Если произошла ошибка при выполнении команды
        if (response instanceof ErrorPacket) {
            System.out.println("ОШИБКА: невозможно выполнить команду REMOVE AUTHOR.\n");
            System.out.println(((ErrorPacket) response).getDescription());
        }

        //Если всё ок
        if (response instanceof OkPacket) {
            System.out.println("Автор удален успешно.\n");
        }
    }
}
