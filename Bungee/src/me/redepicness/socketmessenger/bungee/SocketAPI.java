package me.redepicness.socketmessenger.bungee;

import me.redepicness.socketmessenger.api.Data;
import net.md_5.bungee.api.config.ServerInfo;

public class SocketAPI {

    public static void sendDataToClient(ServerInfo server, String channel, Data data){
        SocketManager.connectedSockets.get(server.getName()).sendCommand(SocketManager.Command.SEND_DATA, channel, data);
    }

}
