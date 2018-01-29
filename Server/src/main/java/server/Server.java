package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    public static void main(String[] args) {

        try {
            ServerSocket serverSocket = new ServerSocket(4444);
            ExecutorService executorService = Executors.newFixedThreadPool(5);
            System.out.println("Starts, waiting for clients");

            while (!serverSocket.isClosed()){
                Socket client = serverSocket.accept();
                executorService.execute(new NewConnection(client));
            }
            executorService.shutdown();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
