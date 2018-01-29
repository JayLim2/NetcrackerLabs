package client.gui;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class Client {

    private int id;
    private static final int SERVER_PORT = 4444;
    private Socket socket;
    private OutputStream out;
    private InputStream in;
    private ClientListener clientListener;
    private static Client instance;

    private Client() {
    }

    public static Client getInstance() {
        if (instance == null)
            instance = new Client();
        return instance;
    }

    public int connect() {
        try {
            socket = new Socket("localhost", SERVER_PORT);
            out = socket.getOutputStream();
            in = socket.getInputStream();
            while (in.available() < 0) {
                try {
                    Thread.sleep(250);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            id = in.read();
            System.out.println("Your id: "+id);
            clientListener = new ClientListener(in);
            clientListener.start();
            return 0;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 1;
    }

    public void finish() {
        try {
            out.close();
            clientListener.interrupt();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public OutputStream getOut() {
        return out;
    }
}
