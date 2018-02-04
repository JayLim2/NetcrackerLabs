package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server extends Thread {
    private ServerSocket serverSocket;
    private ExecutorService executorService;
    private Socket client;

    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(4444);
            executorService = Executors.newFixedThreadPool(5);
            System.out.println("Starts, waiting for clients");

            while (!isInterrupted()) {
                client = serverSocket.accept();
                NewConnection newConnection = new NewConnection(client);
                executorService.execute(newConnection);
                ServerController.getInstance().addClient(newConnection);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void finish() {
        try {
            serverSocket.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            executorService.shutdown();
        }
    }
}
