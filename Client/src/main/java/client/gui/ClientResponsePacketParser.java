package client.gui;

public class ClientResponsePacketParser {
    private static ClientResponsePacketParser instance;

    private ClientResponsePacketParser() {
    }

    public static ClientResponsePacketParser getInstance(){
        if (instance == null){
            instance = new ClientResponsePacketParser();
        }
        return instance;
    }

    public void parse(){
        //todo парсинг команды, выполнение действия

    }
}
