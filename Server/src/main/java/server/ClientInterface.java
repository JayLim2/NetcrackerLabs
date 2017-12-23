/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import controllers.AuthorContainerController;
import controllers.AuthorController;
import controllers.BookController;
import models.Author;
import models.AuthorsContainer;
import models.Book;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import models.YearOutOfBoundsException;
import protocol.Commands;
import protocol.Responses;

/**
 *
 * @author Алескандр
 */
public class ClientInterface implements Runnable{
    private final Socket clientSocket;
    private AuthorContainerController aCC;
    private Lock readLock;
    private Lock writeLock;
    
    public ClientInterface(Socket clientSocket, AuthorContainerController aCC, ReadWriteLock rwl){
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
            JAXBContext contextCommands = JAXBContext.newInstance(Commands.class);
            JAXBContext contextResponses = JAXBContext.newInstance(Responses.class);
            JAXBContext contextBook = JAXBContext.newInstance(Book.class);
            JAXBContext contextAuthor = JAXBContext.newInstance(Author.class);
            JAXBContext contextIndex = JAXBContext.newInstance(Index.class);
            JAXBContext contextAuthorsContainer = JAXBContext.newInstance(AuthorsContainer.class);
            Unmarshaller unmarshCommands = contextCommands.createUnmarshaller();
            Unmarshaller unmarshIndex = contextIndex.createUnmarshaller();
            Unmarshaller unmarshBook = contextBook.createUnmarshaller();
            Unmarshaller unmarshAuthor = contextAuthor.createUnmarshaller();
            Marshaller marshResponses = contextResponses.createMarshaller();
            Marshaller marshAuthorsContainer = contextAuthorsContainer.createMarshaller();
            a:{
                while(true){
                    Commands command = Commands.VIEW_AUTHORS;
                    xer = xmi.createXMLEventReader(inp);
                    xer.nextEvent();
                    xer.peek();
                    command = (Commands)unmarshCommands.unmarshal(xer);
                    switch(command){
                       case VIEW_AUTHORS:
                       case VIEW_BOOKS:
                           readLock.lock();
                           try{
                                //marshResponses.marshal(Responses.OK, outp);
                                marshAuthorsContainer.marshal(aCC.getAuthorsContainer(), outp);
                           }
                           finally{
                               readLock.unlock();
                           }
                           break;
                       case ADD_BOOK:{
                            Index id = (Index)unmarshIndex.unmarshal(inp);
                            Book book = (Book)unmarshBook.unmarshal(inp);
                            writeLock.lock();
                            try{
                                 book.dispatchId();
                                 book.setAuthor(aCC.getAuthor(id.getId()));//надо чтоб в контроллере это ваялось
                                 new  AuthorController(aCC.getAuthor(id.getId())).addBook(0, book);
                                 marshResponses.marshal(Responses.OK, outp);
                            }
                            finally{
                                writeLock.unlock();
                            }
                        }
                        break;
                       case ADD_AUTHOR:
                       {
                           Author author = (Author)unmarshAuthor.unmarshal(inp);
                           writeLock.lock();
                           try{
                                author.dispatchId();
                                aCC.addAuthor(author);
                                marshResponses.marshal(Responses.OK, outp);
                           }
                           finally{
                               writeLock.unlock();
                           }
                       }
                           break;
                       case SET_BOOK:{
                            Index id = (Index)unmarshIndex.unmarshal(inp);
                            Index id2 = (Index)unmarshIndex.unmarshal(inp);
                            Book book = (Book)unmarshBook.unmarshal(inp);
                            writeLock.lock();
                            try{
                                Book destBook = aCC.getBook(id.getId());//должон быть первым. если обломится, то прекращаем редактирование. предотвратит дубликат. так и не вынесли isExsist в контроллер.
                                destBook.setAuthor(aCC.getAuthor(id2.getId()));//неплохо бы вынести все это дело в контроллер
                                destBook.setPublishYear(book.getPublishYear());
                                destBook.setBrief(book.getBrief());
                                destBook.setPublisher(book.getPublisher());
                                destBook.setTitle(book.getTitle());
                            }
                            catch (YearOutOfBoundsException ex) {
                                //вообще произойти не должно. валидация года в клиенте должна быть
                            }   
                            finally{
                                writeLock.unlock();
                            }
                        }
                           break;
                       case SET_AUTHOR:
                       {
                            Index id = (Index)unmarshIndex.unmarshal(inp);
                            Author author = (Author)unmarshAuthor.unmarshal(inp);
                            writeLock.lock();
                            try{
                                aCC.getAuthor(id.getId()).setName(author.getName());
                            } 
                            finally{
                                writeLock.unlock();
                            }
                       }
                            break;
                       case REMOVE_BOOK:
                       {
                            Index id = (Index)unmarshIndex.unmarshal(inp);
                            writeLock.lock();
                            try{
                                 aCC.removeBook(id.getId());
                                 Book.removeId(id.getId());//надо засунуть в контроллер
                                 marshResponses.marshal(Responses.OK, outp);
                            }
                            finally{
                                writeLock.unlock();
                            }
                       }
                       case REMOVE_AUTHOR:
                       {
                            Index id = (Index)unmarshIndex.unmarshal(inp);
                            writeLock.lock();
                            try{
                                 aCC.removeAuthor(id.getId());
                                 Author.removeId(id.getId());//надо засунуть в контроллер
                                 marshResponses.marshal(Responses.OK, outp);
                            }
                            finally{
                                writeLock.unlock();
                            }
                       }    
                       case BYE:{
                           marshResponses.marshal(Responses.OK, outp);
                           break a;
                       }
                       default: marshResponses.marshal(Responses.ERROR, outp);
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
