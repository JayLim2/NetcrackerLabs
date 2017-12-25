package client;

import models.Author;
import models.AuthorsContainer;
import models.Book;
import models.YearOutOfBoundsException;
import protocol.*;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    private static Socket clientSocket;
    private static OutputStream out;
    private static InputStream in;
    private static Marshaller commandMarshaller;
    private static JAXBContext contextCommands;
    private static XMLInputFactory xmi;
    private static XMLEventReader xer;

    public static void main(String[] args) {
        try {
            clientSocket = new Socket(InetAddress.getLocalHost(), 4444);
            out = clientSocket.getOutputStream();
            in = clientSocket.getInputStream();

            contextCommands = JAXBContext.newInstance(CommandPacket.class, AddBookPacket.class, ViewBooksPacket.class, SetBookPacket.class);
            commandMarshaller = contextCommands.createMarshaller();
            xmi = XMLInputFactory.newFactory();

            //======================== VIEW BOOK ==============================
            try {
                viewBooks();
            } catch (XMLStreamException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }

            //======================== ADD BOOK ==============================
            try {
                Book book = new Book("test", null, 1234, "somedude", "a test book");
                addBook(book);
            } catch (XMLStreamException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            } catch (YearOutOfBoundsException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }

            //======================== VIEW BOOK ==============================
            try {
                viewBooks();
            } catch (XMLStreamException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }

            //======================== EDIT BOOK ==============================
            try {
                Book book = new Book("test111", null, 1234, "somedude", "a test book");
                editBook(0, book);
            } catch (XMLStreamException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            } catch (YearOutOfBoundsException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }

            //======================== VIEW BOOK ==============================
            /*try {
                viewBooks();
            } catch (XMLStreamException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }*/

            //======================================================================
            in.close();
            out.close();
            clientSocket.close();
        } catch (UnknownHostException e) {
            System.out.println("Неизвестный хост.");
        } catch (IOException e) {
            System.out.println("Ошибка механизма ввода-вывода.");
            e.printStackTrace();
        } catch (JAXBException e) {
            System.out.println("Ошибка XML-сериализации.");
            e.printStackTrace();
        }
    }

    /**
     * Команда просмотра списка книг
     *
     * @throws JAXBException
     * @throws XMLStreamException
     */
    public static void viewBooks() throws JAXBException, XMLStreamException {
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

            AuthorsContainer authorsContainer = viewBooksResponsePacket.getAuthorsContainer();
            List<Author> authors = authorsContainer.getAuthors();
            for (Author author : authors) {
                List<Book> books = author.getBooks();
                for (Book book : books) {
                    System.out.printf("%4d %30s %5d %15s %25s%n", book.getId(), book.getTitle(), book.getPublishYear(), book.getPublisher(), book.getBrief());
                }
            }
        }
    }

    /**
     * Команда добавления книги
     * @param book
     * @throws JAXBException
     * @throws XMLStreamException
     */
    public static void addBook(Book book) throws JAXBException, XMLStreamException {
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
     * Команда изменения книги
     *
     * @param id
     * @param book
     * @throws JAXBException
     * @throws XMLStreamException
     */
    public static void editBook(int id, Book book) throws JAXBException, XMLStreamException {
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
     * Команда удаления книги
     *
     * @param id
     * @throws JAXBException
     * @throws XMLStreamException
     */
    public static void deleteBook(int id) throws JAXBException, XMLStreamException {
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

    public static void viewAuthors() {

    }

    public static void addAuthor(Author author) {

    }

    public static void editAuthor(int id, String authorName) {

    }

    public static void deleteAuthor(int id) {

    }
}
