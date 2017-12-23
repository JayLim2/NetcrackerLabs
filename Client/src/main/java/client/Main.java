package client;

import models.Author;
import models.AuthorsContainer;
import models.Book;
import protocol.Commands;
import protocol.Responses;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;

public class Main {
    public static void main(String[] args) {
        try {
            Socket clientSocket = new Socket(InetAddress.getLocalHost(), 4444);
            OutputStream OS = clientSocket.getOutputStream();
            InputStream IS = clientSocket.getInputStream();

            Commands currentCommand = Commands.VIEW_BOOKS;
            Responses currentResponse;

            JAXBContext contextCommands = JAXBContext.newInstance(Commands.class);
            JAXBContext contextResponses = JAXBContext.newInstance(Responses.class);
            JAXBContext contextAuthorsContainer = JAXBContext.newInstance(AuthorsContainer.class);
            Marshaller commandMarshaller = contextCommands.createMarshaller();
            Unmarshaller authorsContainerUnmarshaller = contextAuthorsContainer.createUnmarshaller();
            Unmarshaller responseUnmarshaller = contextResponses.createUnmarshaller();

            commandMarshaller.marshal(currentCommand, OS);
//            currentResponse = (Responses) responseUnmarshaller.unmarshal(IS);
//
//            if (currentResponse == Responses.OK) {
                System.out.println("Response accepted.\n");
                XMLInputFactory xmi = XMLInputFactory.newFactory();
                InputStream inp = clientSocket.getInputStream();
                XMLEventReader xer = xmi.createXMLEventReader(IS);
                xer.nextEvent();
                xer.peek();
                AuthorsContainer authorsContainer = (AuthorsContainer) authorsContainerUnmarshaller.unmarshal(xer);
                //AuthorContainerController aCC = new AuthorContainerController(authorsContainer);

                //View books
                List<Author> authors = authorsContainer.getAuthors();
                for (Author author : authors) {
                    List<Book> books = author.getBooks();
                    for (Book book : books) {
                        System.out.printf("%4d %30s %5d %15s %25s%n", book.getId(), book.getTitle(),  book.getPublishYear(), book.getPublisher(), book.getBrief());
                    }
                }
                commandMarshaller.marshal(currentCommand, OS);
                xer = xmi.createXMLEventReader(IS);
                xer.nextEvent();
                 xer.peek();
                 Book.resetId();
                 Author.resetId();
                authorsContainer = (AuthorsContainer) authorsContainerUnmarshaller.unmarshal(xer);

                 authors = authorsContainer.getAuthors();
                for (Author author : authors) {
                    List<Book> books = author.getBooks();
                    for (Book book : books) {
                        System.out.printf("%4d %30s %5d %15s %25s%n", book.getId(), book.getTitle(),  book.getPublishYear(), book.getPublisher(), book.getBrief());
                    }
                }
                //System.out.println("Authors Containter received.");
//            } else {
//                System.out.println("400 - Bad request.");
//            }

            IS.close();
            OS.close();
            clientSocket.close();
        } catch (UnknownHostException e) {
            System.out.println("Неизвестный хост.");
        } catch (IOException e) {
            System.out.println("Ошибка механизма ввода-вывода.");
        } catch (JAXBException e) {
            System.out.println("Ошибка XML-сериализации.");
        } catch (XMLStreamException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
