package client.gui;

import models.Author;
import models.Book;
import protocol.*;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.OutputStream;

public class PacketSender {
    private static PacketSender instance;

    private PacketSender() {
    }

    public static PacketSender getInstance() {
        if (instance == null) {
            instance = new PacketSender();
        }
        return instance;
    }



    public void viewBooks(OutputStream out) throws JAXBException {
        JAXBContext contextCommands = JAXBContext.newInstance(ViewBooksPacket.class);
        Marshaller commandMarshaller = contextCommands.createMarshaller();
        ViewBooksPacket currentCommand = new ViewBooksPacket(Commands.VIEW_BOOKS);
        commandMarshaller.marshal(currentCommand, out);
        //todo flush
    }

    public void addBook(Book book, Author author, OutputStream out) throws JAXBException {
        JAXBContext contextCommands = JAXBContext.newInstance(AddBookPacket.class);
        Marshaller commandMarshaller = contextCommands.createMarshaller();
        AddBookPacket currentCommand = new AddBookPacket(Commands.ADD_BOOK, author.getId(), book);
        commandMarshaller.marshal(currentCommand, out);
    }

    public void editBook(int id, Book book, OutputStream out) throws JAXBException {
        JAXBContext contextCommands = JAXBContext.newInstance(SetBookPacket.class);
        Marshaller commandMarshaller = contextCommands.createMarshaller();
        SetBookPacket currentCommand = new SetBookPacket(Commands.SET_BOOK, book.getAuthor().getId(), id, book);
        commandMarshaller.marshal(currentCommand, out);
    }


    public void deleteBook(int id, OutputStream out) throws JAXBException {
        JAXBContext contextCommands = JAXBContext.newInstance(RemoveBookPacket.class);
        Marshaller commandMarshaller = contextCommands.createMarshaller();
        RemoveBookPacket currentCommand = new RemoveBookPacket(Commands.REMOVE_BOOK, id);
        commandMarshaller.marshal(currentCommand, out);
    }

    public void viewAuthors(OutputStream out) throws JAXBException {
        JAXBContext contextCommands = JAXBContext.newInstance(ViewBooksPacket.class);
        Marshaller commandMarshaller = contextCommands.createMarshaller();
        ViewBooksPacket currentCommand = new ViewBooksPacket(Commands.VIEW_BOOKS);
        commandMarshaller.marshal(currentCommand, out);
    }

    public void addAuthor(String authorName, OutputStream out) throws JAXBException {
        JAXBContext contextCommands = JAXBContext.newInstance(AddAuthorPacket.class);
        Marshaller commandMarshaller = contextCommands.createMarshaller();
        Author author = new Author(authorName);
        AddAuthorPacket currentCommand = new AddAuthorPacket(Commands.ADD_AUTHOR, author);
        commandMarshaller.marshal(currentCommand, out);
    }

    public void editAuthor(int id, String authorName, OutputStream out) throws JAXBException {
        JAXBContext contextCommands = JAXBContext.newInstance(SetAuthorPacket.class);
        Marshaller commandMarshaller = contextCommands.createMarshaller();
        Author author = new Author(authorName);
        SetAuthorPacket currentCommand = new SetAuthorPacket(Commands.SET_AUTHOR, id, author);
        commandMarshaller.marshal(currentCommand, out);
    }

    public void deleteAuthor(int id, OutputStream out) throws JAXBException {
        JAXBContext contextCommands = JAXBContext.newInstance(RemoveAuthorPacket.class);
        Marshaller commandMarshaller = contextCommands.createMarshaller();
        RemoveAuthorPacket currentCommand = new RemoveAuthorPacket(Commands.REMOVE_AUTHOR, id);
        commandMarshaller.marshal(currentCommand, out);
    }
}
