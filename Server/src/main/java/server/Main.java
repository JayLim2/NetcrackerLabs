package server;

import controllers.*;
import views.*;
import models.*;
import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.*;
import javax.xml.bind.annotation.XmlElement;


public class Main {
    
    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(4444);
            ExecutorService exec = Executors.newFixedThreadPool(5);
            AuthorContainerController aCC;
            try {   
                JAXBContext context = JAXBContext.newInstance(AuthorsContainer.class);
                Unmarshaller unmarsh = context.createUnmarshaller();
                AuthorsContainer authors;
                authors = (AuthorsContainer)unmarsh.unmarshal(new File("XML1.xml"));
                aCC = new AuthorContainerController(authors);
                aCC.reInitAuthorsInBooks();
            } catch (JAXBException ex) {
                System.out.println("Default File not found, or corrupted.");
                AuthorsContainer empty = new AuthorsContainer();
                aCC = new AuthorContainerController(empty);
            }
            while(true){
                exec.submit(new ClientInterface(serverSocket.accept(), aCC));
            }
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}


