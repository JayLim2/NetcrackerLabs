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
            currentResponse = (Responses) responseUnmarshaller.unmarshal(IS);

            if (currentResponse == Responses.OK) {
                System.out.println("Response accepted.\n");
                AuthorsContainer authorsContainer = (AuthorsContainer) authorsContainerUnmarshaller.unmarshal(IS);
                //AuthorContainerController aCC = new AuthorContainerController(authorsContainer);

                //View books
                List<Author> authors = authorsContainer.getAuthors();
                for (Author author : authors) {
                    List<Book> books = author.getBooks();
                    for (Book book : books) {
                        System.out.printf("%4d %30s %15s %5d %15s %25s", book.getId(), book.getTitle(), book.getAuthor().getName(), book.getPublishYear(), book.getPublisher(), book.getBrief());
                    }
                }

                //System.out.println("Authors Containter received.");
            } else {
                System.out.println("400 - Bad request.");
            }

            IS.close();
            OS.close();
            clientSocket.close();
        } catch (UnknownHostException e) {
            System.out.println("Неизвестный хост.");
        } catch (IOException e) {
            System.out.println("Ошибка механизма ввода-вывода.");
        } catch (JAXBException e) {
            System.out.println("Ошибка XML-сериализации.");
        }
    }
}
