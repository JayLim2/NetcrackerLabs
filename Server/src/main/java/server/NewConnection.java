package server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class NewConnection extends Thread {

    private Socket socket;
    private OutputStream out;
    private InputStream in;
    private int clientId;


    public NewConnection(Socket client) {
        this.socket = client;
    }

    public int getClientId() {
        return clientId;
    }

    @Override
    public void run() {
        try {
            out = socket.getOutputStream();
            in = socket.getInputStream();
            clientId = IdGenerator.getInstance().createId();
            StreamContainer.getInstance().addStream(clientId, out);
            StreamContainer.getInstance().View();
            out.write(clientId);
            out.flush();
            System.out.println("New client # " + clientId + " connected");
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

    //todo захуярить commandRelay, аналог листнера, тут только вызвать
}

