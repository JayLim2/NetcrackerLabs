package server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class NewConnection extends Thread {

    private static Socket socket;
    private OutputStream out;
    private InputStream in;
    private int clientId;


    public NewConnection(Socket client) {
        NewConnection.socket = client;
    }

    public int getClientId() {
        return clientId;
    }

    @Override
    public void run() {
        try {
            out = socket.getOutputStream();
            in = socket.getInputStream();
            StreamContainer.getInstance().addStream(out);
            clientId = IdGenerator.getInstance().createId();
            out.write(clientId);
            out.flush();
            System.out.println("New client # "+ clientId + " connected");
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
            System.out.println("Client disconnected");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

