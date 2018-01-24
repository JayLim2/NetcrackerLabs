package client.gui;

import protocol.ErrorPacket;
import protocol.OkPacket;
import protocol.ResponsePacket;
import protocol.ViewBooksResponsePacket;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.io.InputStream;

public class ClientListener extends Thread {

    private InputStream inputStream;
    JAXBContext contextResponsePacket;
    Unmarshaller unmarshResponsePacket;
    XMLInputFactory xmi;
    XMLEventReader xer;

    public ClientListener(InputStream inputStream) {
        try {
            this.inputStream = inputStream;
            contextResponsePacket = JAXBContext.newInstance(ResponsePacket.class, OkPacket.class, ErrorPacket.class, ViewBooksResponsePacket.class);
            unmarshResponsePacket = contextResponsePacket.createUnmarshaller();
            xmi = XMLInputFactory.newFactory();
            xer = xmi.createXMLEventReader(inputStream);
        } catch (XMLStreamException e) {
            e.printStackTrace();
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
                    xer.nextEvent();
                    xer.peek();
                    ResponsePacket response = (ResponsePacket) unmarshResponsePacket.unmarshal(xer);
                    System.out.println("Response accepted.\n");
                    //todo response action, call ClientResponsePacketParser
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XMLStreamException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            closeInputStream();
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

    private void closeInputStream() {
        try {
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
