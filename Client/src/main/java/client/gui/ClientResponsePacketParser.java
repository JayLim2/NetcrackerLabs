package client.gui;

import protocol.ResponsePacket;

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

    public void parse(ResponsePacket responsePacket){
        //todo парсинг команды, выполнение действия
        //из каждой херни вытащить response через if ок ерор вьюбукс

    }
}
