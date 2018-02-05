/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import controllers.AuthorContainerController;
import exceptions.InvalidCommandAction;
import java.io.File;
import java.io.FileOutputStream;
import models.Author;
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
import java.net.Socket;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.logging.Level;
import java.util.logging.Logger;
import models.AuthorsContainer;

import models.BookAlreadyExistsException;

/**
 * Client dialog class.
 * Used for communication with a client. 
 * Accepts command packets, parses them and then 
 * constructs and sends an appropriate response packet.
 * packets are xml formatted
 * @author Alexander
 */
public class ClientInterface implements Runnable {
    private final Socket clientSocket;
    private AuthorContainerController aCC;
    private Lock readLock;
    private Lock writeLock;

    /**
     * Main constructor for the class
     * @param clientSocket the socket to be used in this instance
     * @param aCC the main database container
     * @param rwl synchronisation read/write lock. all instances working with the same aCC are to receive
     * the same lock as well
     */
    public ClientInterface(Socket clientSocket, AuthorContainerController aCC, ReadWriteLock rwl) {
        this.clientSocket = clientSocket;
        this.aCC = aCC;
        readLock = rwl.readLock();
        writeLock = rwl.writeLock();
    }

    /**
     * the main dialog.
     * infinite loop until BYE or disconnect.
     * uses JAXB annotations to pass packets to client
     * currently implemented commands are:
     * ADD_AUTHOR, ADD_BOOK, SET_AUTHOR, SET_BOOK, REMOVE_BOOK, 
     * REMOVE_AUTHOR, VIEW_BOOKS, VIEW_AUTHORS, BYE, SEARCH
     * to know more about their format and functionality
     * @see protocol
     */
    @Override
    public void run() {
        try {
            OutputStream outp = clientSocket.getOutputStream();
            XMLInputFactory xmi = XMLInputFactory.newFactory();
            InputStream inp = clientSocket.getInputStream();
            XMLEventReader xer;
            JAXBContext contextAC = JAXBContext.newInstance(AuthorsContainer.class);
            Marshaller marshAC = contextAC.createMarshaller();
            JAXBContext contextPacket = JAXBContext.newInstance(CommandPacket.class, AddBookPacket.class, 
                    ViewBooksPacket.class,AddAuthorPacket.class, RemoveBookPacket.class,
                    RemoveAuthorPacket.class, SetAuthorPacket.class, SetBookPacket.class, SearchPacket.class);
            Unmarshaller unmarshPacket = contextPacket.createUnmarshaller();
            JAXBContext contextResponse = JAXBContext.newInstance(ResponsePacket.class, OkPacket.class, 
                    ErrorPacket.class,ViewBooksResponsePacket.class);
            Marshaller marshResponse = contextResponse.createMarshaller();
            a:
            {
                while (true) {
                    CommandPacket command;
                    xer = xmi.createXMLEventReader(inp);
                    xer.nextEvent();
                    xer.peek();
                    String res = xer.toString();
                    command = (CommandPacket) unmarshPacket.unmarshal(xer);
                    switch (command.getCommand()) {
                        case VIEW_AUTHORS:
                        case VIEW_BOOKS:
                            readLock.lock();
                            try {
                                marshResponse.marshal(new ViewBooksResponsePacket(Responses.OK, aCC.getAuthorsContainer()), outp);
                            } finally {
                                readLock.unlock();
                            }
                            break;
                        case ADD_BOOK: {
                            AddBookPacket adbp = (AddBookPacket) command;
                            int id = adbp.getId();
                            Book book = adbp.getBook();
                            writeLock.lock();
                            try {
                                aCC.addBook(book, id);
                                marshResponse.marshal(new OkPacket(Responses.OK), outp);
                                /*for (OutputStream stream : StreamContainer.getInstance().getStreams())
                                    marshResponse.marshal(new ViewBooksResponsePacket(Responses.OK, aCC.getAuthorsContainer()), outp);*/
                                marshResponse.marshal(new OkPacket(Responses.OK), outp);
                                marshAC.marshal(aCC.getAuthorsContainer(), new FileOutputStream("XML1a.xml"));
                            } catch (IndexOutOfBoundsException ex) {
                                marshResponse.marshal(new ErrorPacket(Responses.ERROR, ex.getMessage()), outp);
                            } catch (BookAlreadyExistsException ex) {
                                marshResponse.marshal(new ErrorPacket(Responses.ERROR, "Duplicate Book error"), outp);
                            } finally {
                                writeLock.unlock();
                            }
                        }
                        break;
                        case ADD_AUTHOR: {
                            AddAuthorPacket abap = (AddAuthorPacket) command;
                            writeLock.lock();
                            try {
                                Author author = abap.getAuthor();
                                aCC.addAuthor(author);
                                marshResponse.marshal(new OkPacket(Responses.OK), outp);
                                marshAC.marshal(aCC.getAuthorsContainer(), new FileOutputStream("XML1a.xml"));
                            } catch (InvalidCommandAction e) {
                                marshResponse.marshal(new ErrorPacket(Responses.ERROR, e.getMessage()), outp);
                            } finally {
                                writeLock.unlock();
                            }
                        }
                        break;
                        case SET_BOOK: {
                            SetBookPacket stbp = (SetBookPacket) command;
                            Book book = stbp.getBook();
                            int id = stbp.getBookId();
                            int id2 = stbp.getNewAuthorId();
                            writeLock.lock();
                            try {
                                aCC.changeBook(book, id, id2);
                                marshResponse.marshal(new OkPacket(Responses.OK), outp);
                                marshAC.marshal(aCC.getAuthorsContainer(), new FileOutputStream("XML1a.xml"));
                            } catch (YearOutOfBoundsException ex) {
                                //вообще произойти не должно. валидация года в клиенте должна быть
                                marshResponse.marshal(new ErrorPacket(Responses.ERROR, "Year error"), outp);
                            } catch (IndexOutOfBoundsException ex) {
                                marshResponse.marshal(new ErrorPacket(Responses.ERROR, ex.getMessage()), outp);
                            } catch (BookAlreadyExistsException ex) {
                                marshResponse.marshal(new ErrorPacket(Responses.ERROR, "Duplicate Book error"), outp);
                            } finally {
                                writeLock.unlock();
                            }
                        }
                        break;
                        case SET_AUTHOR: {
                            SetAuthorPacket stap = (SetAuthorPacket) command;
                            writeLock.lock();
                            try {
                                aCC.getAuthor(stap.getId()).setName(stap.getAuthor().getName());
                                marshResponse.marshal(new OkPacket(Responses.OK), outp);
                                marshAC.marshal(aCC.getAuthorsContainer(), new FileOutputStream("XML1a.xml"));
                            } catch (IndexOutOfBoundsException ex) {
                                marshResponse.marshal(new ErrorPacket(Responses.ERROR, ex.getMessage()), outp);
                            } finally {
                                writeLock.unlock();
                            }
                        }
                        break;
                        case REMOVE_BOOK: {
                            RemoveBookPacket rmbp = (RemoveBookPacket) command;
                            writeLock.lock();
                            try {
                                aCC.removeBook(rmbp.getId());
                                marshResponse.marshal(new OkPacket(Responses.OK), outp);
                                marshAC.marshal(aCC.getAuthorsContainer(), new FileOutputStream("XML1a.xml"));
                            } catch (IndexOutOfBoundsException ex) {
                                marshResponse.marshal(new ErrorPacket(Responses.ERROR, ex.getMessage()), outp);
                            } finally {
                                writeLock.unlock();
                            }
                        }
                        break;
                        case REMOVE_AUTHOR: {
                            RemoveAuthorPacket rmap = (RemoveAuthorPacket) command;
                            writeLock.lock();
                            try {
                                aCC.removeAuthor(rmap.getId());
                                marshResponse.marshal(new OkPacket(Responses.OK), outp);
                                marshAC.marshal(aCC.getAuthorsContainer(), new FileOutputStream("XML1a.xml"));
                            } catch (IndexOutOfBoundsException ex) {
                                marshResponse.marshal(new ErrorPacket(Responses.ERROR, ex.getMessage()), outp);
                            } finally {
                                writeLock.unlock();
                            }
                        }
                        break;
                        case SEARCH:{
                            SearchPacket searchPacket = (SearchPacket)command;
                            readLock.lock();
                            try{
                                AuthorsContainer resp = aCC.search(searchPacket.getBookFilter());
                                marshResponse.marshal(new ViewBooksResponsePacket(Responses.OK,resp), outp);
                            }
                            finally{
                                readLock.unlock();
                            }
                        }
                        break;
                        case BYE: {
                            marshResponse.marshal(new OkPacket(Responses.OK), outp);
                            break a;
                        }
                        default:
                            marshResponse.marshal(new ErrorPacket(Responses.ERROR, "unknownCommand"), outp);
                    }
                }
            }
        } catch (JAXBException ex) {
            //shouldnt happen
        } catch (IOException ex) {
            //shoudnt happen
        } catch (XMLStreamException ex) {
            //shoudnt happen
        }
    }

}
