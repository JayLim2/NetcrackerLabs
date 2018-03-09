package client.gui;

import controllers.AuthorContainerController;
import models.AuthorsContainer;
import protocol.ErrorPacket;
import protocol.OkPacket;
import protocol.ResponsePacket;
import protocol.ViewBooksResponsePacket;

public class ClientResponsePacketParser {
    private static ClientResponsePacketParser instance;

    private ClientResponsePacketParser() {
    }

    public static ClientResponsePacketParser getInstance() {
        if (instance == null) {
            instance = new ClientResponsePacketParser();
        }
        return instance;
    }

    public void parse(ResponsePacket responsePacket) {
        //todo парсинг команды, выполнение действия
        if (responsePacket instanceof OkPacket) {
            System.out.println("Done.\n");
        }
        if (responsePacket instanceof ErrorPacket) {
            System.out.println("Error");
        }
        if (responsePacket instanceof ViewBooksResponsePacket) {



        }

    }


}
