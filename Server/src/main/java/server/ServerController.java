package server;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class ServerController {
    private Map<Integer, NewConnection> clientsContainer;
    private static ServerController instance;
    private Server server;

    private ServerController() {
        clientsContainer = new HashMap<>();
        server = new Server();
        server.start();
    }

    public static ServerController getInstance() {
        if (instance == null)
            instance = new ServerController();
        return instance;
    }

    public void finishServer(){
        server.interrupt();
        server.finish();
        finishAllClients();
        System.out.println("Server shutdown");
    }

    public void addClient(NewConnection newConnection) {
        clientsContainer.put(newConnection.getClientId(), newConnection);
    }

    public void finishClient(int clientId) {
        StreamContainer.getInstance().removeStream(clientId);
        clientsContainer.get(clientId).interrupt();
        clientsContainer.get(clientId).finish();
        clientsContainer.remove(clientId);
    }

    private void finishAllClients() {
        for (NewConnection client : clientsContainer.values()) {
            finishClient(client.getClientId());
        }
    }

    public static void main(String[] args) {
        ServerController.getInstance();
        Scanner sc = new Scanner(System.in);
        if (sc.nextInt() == 1) {
            ServerController.getInstance().finishServer();
        }
    }


}
