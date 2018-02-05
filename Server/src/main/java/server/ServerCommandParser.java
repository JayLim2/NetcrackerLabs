package server;

import controllers.AuthorContainerController;
import exceptions.InvalidCommandAction;
import models.*;
import protocol.*;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;

public class ServerCommandParser {


    private static ServerCommandParser instance;
    private OutputStream outp;
    private AuthorContainerController aCC;
    private Lock readLock;
    private Lock writeLock;
    private Marshaller marshResponse;
    private JAXBContext contextAC;
    private Marshaller marshAC;

    public ServerCommandParser() {

    }

    public static ServerCommandParser getInstance() {
        if (instance == null) {
            instance = new ServerCommandParser();
        }
        return instance;
    }

    public synchronized void parse(CommandPacket command) throws JAXBException {
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
                    marshAC.marshal(aCC.getAuthorsContainer(), new FileOutputStream("XML1a.xml"));
                    sendUpdateCommand();
                } catch (IndexOutOfBoundsException ex) {
                    marshResponse.marshal(new ErrorPacket(Responses.ERROR, ex.getMessage()), outp);
                } catch (BookAlreadyExistsException ex) {
                    marshResponse.marshal(new ErrorPacket(Responses.ERROR, "Duplicate Book error"), outp);
                } catch (FileNotFoundException ex) {
                    marshResponse.marshal(new ErrorPacket(Responses.ERROR, "File not found"), outp);
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
                    sendUpdateCommand();
                } catch (InvalidCommandAction e) {
                    marshResponse.marshal(new ErrorPacket(Responses.ERROR, e.getMessage()), outp);
                } catch (FileNotFoundException ex) {
                    marshResponse.marshal(new ErrorPacket(Responses.ERROR, "File not found"), outp);
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
                    sendUpdateCommand();
                } catch (YearOutOfBoundsException ex) {
                    //вообще произойти не должно. валидация года в клиенте должна быть
                    marshResponse.marshal(new ErrorPacket(Responses.ERROR, "Year error"), outp);
                } catch (IndexOutOfBoundsException ex) {
                    marshResponse.marshal(new ErrorPacket(Responses.ERROR, ex.getMessage()), outp);
                } catch (BookAlreadyExistsException ex) {
                    marshResponse.marshal(new ErrorPacket(Responses.ERROR, "Duplicate Book error"), outp);
                } catch (FileNotFoundException ex) {
                    marshResponse.marshal(new ErrorPacket(Responses.ERROR, "File not found"), outp);
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
                    sendUpdateCommand();
                } catch (IndexOutOfBoundsException ex) {
                    marshResponse.marshal(new ErrorPacket(Responses.ERROR, ex.getMessage()), outp);
                } catch (FileNotFoundException ex) {
                    marshResponse.marshal(new ErrorPacket(Responses.ERROR, "File not found"), outp);
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
                    sendUpdateCommand();
                } catch (IndexOutOfBoundsException ex) {
                    marshResponse.marshal(new ErrorPacket(Responses.ERROR, ex.getMessage()), outp);
                } catch (FileNotFoundException ex) {
                    marshResponse.marshal(new ErrorPacket(Responses.ERROR, "File not found"), outp);
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
                    sendUpdateCommand();
                } catch (IndexOutOfBoundsException ex) {
                    marshResponse.marshal(new ErrorPacket(Responses.ERROR, ex.getMessage()), outp);
                } catch (FileNotFoundException ex) {
                    marshResponse.marshal(new ErrorPacket(Responses.ERROR, "File not found"), outp);
                } finally {
                    writeLock.unlock();
                }
            }
            break;
            case SEARCH: {
                SearchPacket searchPacket = (SearchPacket) command;
                readLock.lock();
                try {
                    AuthorsContainer resp = aCC.search(searchPacket.getBookFilter());
                    marshResponse.marshal(new ViewBooksResponsePacket(Responses.OK, resp), outp);
                } finally {
                    readLock.unlock();
                }
            }
            break;
            case BYE: {
                marshResponse.marshal(new OkPacket(Responses.OK), outp);
            }
            default:
                marshResponse.marshal(new ErrorPacket(Responses.ERROR, "unknownCommand"), outp);
        }

    }

    private void sendUpdateCommand() {
        List<OutputStream> streams = StreamContainer.getInstance().getStreams();
        if (streams != null) {
            for (OutputStream stream : streams) {
                try {
                    marshResponse.marshal(new ViewBooksResponsePacket(Responses.OK, aCC.getAuthorsContainer()), stream);
                } catch (JAXBException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("Tables updated");
        }
    }
}





