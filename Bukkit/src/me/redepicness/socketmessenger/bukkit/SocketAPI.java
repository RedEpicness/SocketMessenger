package me.redepicness.socketmessenger.bukkit;

import me.redepicness.socketmessenger.api.Data;

public class SocketAPI {

    public static void sendDataToServer(String channel, Data data){
        SocketManager.sendCommand(SocketManager.Command.SEND_DATA, channel, data);
    }

    public static void broadcastToServer(String msg){
        SocketManager.sendCommand(SocketManager.Command.BROADCAST, msg);
    }

}
