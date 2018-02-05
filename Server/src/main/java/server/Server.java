package server;

import controllers.AuthorContainerController;
import models.AuthorsContainer;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Server extends Thread {
    private ServerSocket serverSocket;
    private ExecutorService executorService;
    private Socket client;

    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(4444);
            executorService = Executors.newFixedThreadPool(5);
            System.out.println("Starts, waiting for clients");

            AuthorContainerController aCC;
            Unmarshaller unmarsh = null;
            try {
                JAXBContext context = JAXBContext.newInstance(AuthorsContainer.class);
                unmarsh = context.createUnmarshaller();
            } catch (JAXBException e) {
                e.printStackTrace();
            }
            AuthorsContainer authors;
            try {
                authors = (AuthorsContainer)unmarsh.unmarshal(new File("XML1a.xml"));
                aCC = new AuthorContainerController(authors);
                aCC.reInitAuthorsInBooks();
                aCC.resolveIds();
            } catch (JAXBException ex) {
                System.out.println("Default File not found, or corrupted.");
                try {
                    authors = (AuthorsContainer)unmarsh.unmarshal(Main.class.getResourceAsStream("/XML1.xml"));
                    aCC = new AuthorContainerController(authors);
                    aCC.reInitAuthorsInBooks();
                    aCC.resolveIds();
                } catch (JAXBException ex2) {
                    System.out.println("Backup File not found, or corrupted.");
                    AuthorsContainer empty = new AuthorsContainer();
                    aCC = new AuthorContainerController(empty);
                }
            }
            ReadWriteLock rwl = new ReentrantReadWriteLock() ;

            executorService.submit(new ServerControl(aCC, rwl));

            while (!isInterrupted()) {
                client = serverSocket.accept();
                NewConnection newConnection = new NewConnection(client);
                executorService.execute(newConnection);
                ServerController.getInstance().addClient(newConnection);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void finish() {
        try {
            serverSocket.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            executorService.shutdown();
        }
    }
}
