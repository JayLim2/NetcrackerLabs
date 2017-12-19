/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import controllers.AuthorContainerController;
import controllers.AuthorController;
import models.Author;
import models.AuthorsContainer;
import models.Book;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import protocol.Commands;
import protocol.Responses;

/**
 *
 * @author Алескандр
 */
public class ClientInterface implements Runnable{
    private final Socket clientSocket;
    private AuthorContainerController aCC;
    
    public ClientInterface(Socket clientSocket, AuthorContainerController aCC){
        this.clientSocket = clientSocket;
        this.aCC = aCC;
    }
    
    @Override
    public void run() {
        try {
            OutputStream outp = clientSocket.getOutputStream();
            InputStream inp = clientSocket.getInputStream();
            JAXBContext contextCommands = JAXBContext.newInstance(Commands.class);
            JAXBContext contextResponses = JAXBContext.newInstance(Responses.class);
            JAXBContext contextBook = JAXBContext.newInstance(Book.class);
            JAXBContext contextAuthor = JAXBContext.newInstance(Author.class);
            JAXBContext contextIndex = JAXBContext.newInstance(Index.class);
            JAXBContext contextAuthorsContainer = JAXBContext.newInstance(AuthorsContainer.class);
            Unmarshaller unmarshCommands = contextCommands.createUnmarshaller();
            Unmarshaller unmarshIndex = contextIndex.createUnmarshaller();
            Unmarshaller unmarshBook = contextBook.createUnmarshaller();
            Marshaller marshResponses = contextResponses.createMarshaller();
            Marshaller marshAuthorsContainer = contextAuthorsContainer.createMarshaller();
            while(true){
                Commands command = (Commands)unmarshCommands.unmarshal(inp);
                switch(command){
                   case VIEW_BOOKS:
                       marshResponses.marshal(Responses.OK, outp);
                       marshAuthorsContainer.marshal(aCC.getAuthorsContainer(), outp);
                       break;
                   case ADD_BOOK:
                   case ADD_AUTHOR:
                   case SET_BOOK:
                   case SET_AUTHOR:
                   case REMOVE_BOOK:
                   case REMOVE_AUTHOR:
                   case VIEW_AUTHORS:
                   case BYE:
                   default: marshResponses.marshal(Responses.ERROR, outp);
                }
            }
        } catch (JAXBException ex) {
            Logger.getLogger(ClientInterface.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ClientInterface.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
