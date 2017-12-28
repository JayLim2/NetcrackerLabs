/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import controllers.AuthorContainerController;
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
import models.BookAlreadyExistsException;

/**
 * @author Алескандр
 */
public class ClientInterface implements Runnable {
    private final Socket clientSocket;
    private AuthorContainerController aCC;
    private Lock readLock;
    private Lock writeLock;

    public ClientInterface(Socket clientSocket, AuthorContainerController aCC, ReadWriteLock rwl) {
        this.clientSocket = clientSocket;
        this.aCC = aCC;
        readLock = rwl.readLock();
        writeLock = rwl.writeLock();
    }

    @Override
    public void run() {
        try {
            OutputStream outp = clientSocket.getOutputStream();
            XMLInputFactory xmi = XMLInputFactory.newFactory();
            InputStream inp = clientSocket.getInputStream();
            XMLEventReader xer;
            JAXBContext contextPacket = JAXBContext.newInstance(CommandPacket.class, AddBookPacket.class, ViewBooksPacket.class,
                    AddAuthorPacket.class, RemoveBookPacket.class, RemoveAuthorPacket.class, SetAuthorPacket.class, SetBookPacket.class);
            Unmarshaller unmarshPacket = contextPacket.createUnmarshaller();
//            JAXBContext contextCommands = JAXBContext.newInstance(Commands.class);
            JAXBContext contextResponse = JAXBContext.newInstance(ResponsePacket.class, OkPacket.class, ErrorPacket.class,
                    ViewBooksResponsePacket.class);
//            JAXBContext contextBook = JAXBContext.newInstance(Book.class);
//            JAXBContext contextAuthor = JAXBContext.newInstance(Author.class);
//            JAXBContext contextIndex = JAXBContext.newInstance(Index.class);
            //JAXBContext contextAuthorsContainer = JAXBContext.newInstance(AuthorsContainer.class);
//            Unmarshaller unmarshCommands = contextCommands.createUnmarshaller();
//            Unmarshaller unmarshIndex = contextIndex.createUnmarshaller();
//            Unmarshaller unmarshBook = contextBook.createUnmarshaller();
//            Unmarshaller unmarshAuthor = contextAuthor.createUnmarshaller();
            Marshaller marshResponse = contextResponse.createMarshaller();
            //Marshaller marshAuthorsContainer = contextAuthorsContainer.createMarshaller();
            a:
            {
                while (true) {
                    CommandPacket command = new CommandPacket();
                    xer = xmi.createXMLEventReader(inp);
                    xer.nextEvent();
                    xer.peek();
                    command = (CommandPacket) unmarshPacket.unmarshal(xer);
                    switch (command.getCommand()) {
                        case VIEW_AUTHORS:
                        case VIEW_BOOKS:
                            readLock.lock();
                            try {
                                //marshResponses.marshal(Responses.OK, outp);
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
                                aCC.changeBook(book, id, id2);//мб предварительную валидацию года приписать
                                marshResponse.marshal(new OkPacket(Responses.OK), outp);
                            } catch (YearOutOfBoundsException ex) {
                                //вообще произойти не должно. валидация года в клиенте должна быть
                                marshResponse.marshal(new ErrorPacket(Responses.ERROR, "Year error"), outp);
                            } catch (IndexOutOfBoundsException ex) {
                                marshResponse.marshal(new ErrorPacket(Responses.ERROR, "Index error"), outp);
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
                            }catch (IndexOutOfBoundsException ex) {
                                marshResponse.marshal(new ErrorPacket(Responses.ERROR, "no Author with such index"), outp);
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
                            } catch (IndexOutOfBoundsException ex) {
                                marshResponse.marshal(new ErrorPacket(Responses.ERROR, "no Author with such index"), outp);
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
                            } catch (IndexOutOfBoundsException ex) {
                                marshResponse.marshal(new ErrorPacket(Responses.ERROR, "no Author with such index"), outp);
                            } finally {
                                writeLock.unlock();
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
            Logger.getLogger(ClientInterface.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ClientInterface.class.getName()).log(Level.SEVERE, null, ex);
        } catch (XMLStreamException ex) {
            Logger.getLogger(ClientInterface.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
