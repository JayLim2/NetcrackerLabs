package server;

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

public class ServerListener extends Thread {

    private Socket socket;
    private OutputStream out;
    private InputStream inputStream;
    private Marshaller marshResponse;
    private XMLEventReader xer;
    private XMLInputFactory xmi;
    private JAXBContext contextPacket;
    private JAXBContext contextResponse;
    private Unmarshaller unmarshPacket;
    private ServerCommandParser serverCommandParser;
    private CommandPacket command;

    public ServerListener(InputStream inputStream) {


        try {
            this.inputStream = inputStream;
            contextPacket = JAXBContext.newInstance(CommandPacket.class, AddBookPacket.class, ViewBooksPacket.class,
                    AddAuthorPacket.class, RemoveBookPacket.class, RemoveAuthorPacket.class, SetAuthorPacket.class, SetBookPacket.class);
            unmarshPacket = contextPacket.createUnmarshaller();
            xmi = XMLInputFactory.newFactory();
            contextResponse = JAXBContext.newInstance(ResponsePacket.class, OkPacket.class, ErrorPacket.class,
                    ViewBooksResponsePacket.class);
            marshResponse = contextResponse.createMarshaller();

            serverCommandParser = ServerCommandParser.getInstance();
        } catch (JAXBException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void run() {

        try {
            while (!isInterrupted()) {
                Thread.sleep(250);
                if (inputStream.available() > 0) {

                    xer = xmi.createXMLEventReader(inputStream);
                    xer.nextEvent();
                    xer.peek();
                    command = (CommandPacket) unmarshPacket.unmarshal(xer);
                    serverCommandParser.parse(command);
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XMLStreamException e) {
            e.printStackTrace();
        } catch (JAXBException e) {
            e.printStackTrace();
        }


    }
}
