package me.redepicness.socketmessenger.bukkit;

import me.redepicness.socketmessenger.api.Data;

/**
 * Api for sending/requesting data trough sockets.
 */
public class SocketAPI {

    /**
     * Send a Data object to the server on the specified channel.
     *
     * Retrieve the data on BungeeCord by listening to the ReceivedDataEvent.
     *
     * @param channel - The channel to send the Data trough
     * @param data - The Data to send
     */
    public static void sendDataToServer(String channel, Data data){
        SocketManager.sendCommand(SocketManager.Command.SEND_DATA, channel, data);
    }

    /**
     * Broadcasts a message to everyone connected to the BungeeCord server.
     *
     * @param msg - The message to broadcast
     */
    public static void broadcastToServer(String msg){
        SocketManager.sendCommand(SocketManager.Command.BROADCAST, msg);
    }

    /**
     * Forward a Data object to a specified server on the BungeeCord server.
     *
     * The receiving server can retrieve Data by listening to ReceivedDataEvent.
     *
     * Fails silently if server is offline or not valid.
     *
     * @param server - The server to forward the Data to
     * @param data - The Data to forward
     */
    public static void forwardDataToServer(String server, Data data){
        SocketManager.sendCommand(SocketManager.Command.FORWARD_DATA, server, data);
    }

    /**
     * Connects a specified player to a specified server on the BungeeCord network.
     *
     * Fails silently if player is not online or the server is invalid.
     *
     * @param player - The player to connect
     * @param server - The server to connect to
     */
    public static void connectPlayerToServer(String player, String server){
        SocketManager.sendCommand(SocketManager.Command.CONNECT, player, server);
    }

    /**
     * Requests the player count on the BungeeCord network.
     *
     * The count is sent back as a Data object trough ReceivedDataEvent on channel 'PlayerCount'.
     * Retrieve the count (int) by calling getInt("playerCount") on the received data object.
     */
    public static void requestPlayerCount(){
        SocketManager.sendCommand(SocketManager.Command.PLAYER_COUNT);
    }

    /**
     * Requests the list of online players on the BungeeCord network.
     *
     * The list is sent back as a Data object trough ReceivedDataEvent on channel 'PlayerList'.
     * Retrieve the list (ArrayList<String>) by calling getObject("playerList") on the received
     * data object and casting the result to an ArrayList<String>.
     */
    public static void requestPlayerList(){
        SocketManager.sendCommand(SocketManager.Command.PLAYER_LIST);
    }

    /**
     * Requests the list of registered servers on the BungeeCord network.
     *
     * The list is sent back as a Data object trough ReceivedDataEvent on channel 'ServerList'.
     * Retrieve the list (ArrayList<String>) by calling getObject("serverList") on the received
     * data object and casting the result to an ArrayList<String>.
     */
    public static void requestServerList(){
        SocketManager.sendCommand(SocketManager.Command.GET_SERVERS);
    }

    /**
     * Sends a message to a specified player on the BungeeCord network.
     *
     * Fails silently if player is not online.
     *
     * @param player - The player to message
     * @param message - The message to send
     */
    public static void messagePlayer(String player, String message){
        SocketManager.sendCommand(SocketManager.Command.MESSAGE, player, message);
    }

    /**
     * Requests the name of this server on the BungeeCord network (as configured in config.yml).
     *
     * The name is sent back as a Data object trough ReceivedDataEvent on channel 'ServerName'.
     * Retrieve the name (String) by calling getString("serverName") on the received data object.
     */
    public static void requestServerName(){
        SocketManager.sendCommand(SocketManager.Command.GET_SERVER);
    }

    /**
     * Kicks a specified player on the BungeeCord network with a message.
     *
     * Fails silently if player is not online.
     *
     * @param player - The player to kick
     * @param message - The kick message
     */
    public static void kickPlayer(String player, String message){
        SocketManager.sendCommand(SocketManager.Command.KICK_PLAYER, player, message);
    }

}
