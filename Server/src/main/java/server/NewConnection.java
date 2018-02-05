package server;

import protocol.*;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class NewConnection extends Thread {

    private Socket socket;
    private OutputStream out;
    private InputStream in;
    private int clientId;
    private ServerListener serverListener;


    public NewConnection(Socket client) {
        this.socket = client;
    }

    public int getClientId() {
        return clientId;
    }

    @Override
    public void run() {
        try {
            in = socket.getInputStream();
            clientId = IdGenerator.getInstance().createId();
            StreamContainer.getInstance().addStream(clientId, out);
            out.write(clientId);
            out.flush();
            System.out.println("New client # " + clientId + " connected");
            serverListener = new ServerListener(in);
            serverListener.start();
            //todo вызвать нижний туду
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (isInterrupted())
            finish();
    }

    public void finish() {
        try {
            out.close();
            in.close();
            socket.close();
            System.out.println("Client " + clientId + " disconnected");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //todo захуярить commandRelay, аналог листнера, тут только вызвать  В ОТДЕЛЬНЫЙ КЛАСС ЭТО ВСЕ SERVER LISTENER

}

