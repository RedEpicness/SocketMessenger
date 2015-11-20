package me.redepicness.socketmessenger.bungee;

import net.md_5.bungee.BungeeCord;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;

class SocketManager {

    static final HashMap<String, SocketClient> connectedSockets = new HashMap<>();
    static int ID = 0;
    private static ServerSocket serverSocket;

    static void init(int port){
        try {
            Util.log("Listening for socket connections on port "+port+"!");
            serverSocket = new ServerSocket(port);
            @SuppressWarnings("deprecation")
            ExecutorService service = BungeeCord.getInstance().getPluginManager().getPlugin("SocketMessenger").getExecutorService();
            service.submit(() -> {
                while(!serverSocket.isClosed()){
                    try {
                        Socket socket = serverSocket.accept();
                        service.submit(() -> initSocket(socket));
                    } catch (IOException e) {
                        if(e.getMessage().toLowerCase().contains("socket closed")) return;
                        e.printStackTrace();
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static void end(){
        try {
            if(!serverSocket.isClosed()) serverSocket.close();
            connectedSockets.values().forEach(s -> s.sendCommand(Command.EXIT));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void initSocket(Socket socket){
        ID++;
        Util.log("Socket connected! ID: "+ID);
        new SocketClient(socket);
    }

    enum Command{

        EXIT, IDENTIFY, BROADCAST, SEND_DATA, FORWARD_DATA, CONNECT, PLAYER_COUNT, PLAYER_LIST, GET_SERVERS, MESSAGE, GET_SERVER, KICK_PLAYER;

        public static Command get(byte command){
            switch(command){
                case 0:
                    return EXIT;
                case 1:
                    return BROADCAST;
                case 2:
                    return SEND_DATA;
                case 3:
                    return FORWARD_DATA;
                case 4:
                    return CONNECT;
                case 5:
                    return PLAYER_COUNT;
                case 6:
                    return PLAYER_LIST;
                case 7:
                    return GET_SERVERS;
                case 8:
                    return MESSAGE;
                case 9:
                    return GET_SERVER;
                case 10:
                    return KICK_PLAYER;
                case 127:
                    return IDENTIFY;
                default:
                    throw new RuntimeException("invalid command byte!");
            }
        }

        public byte getByte(){
            switch(this){
                case EXIT:
                    return 0;
                case IDENTIFY:
                    return 127;
                case BROADCAST:
                    return 1;
                case SEND_DATA:
                    return 2;
                case FORWARD_DATA:
                    return 3;
                case CONNECT:
                    return 4;
                case PLAYER_COUNT:
                    return 5;
                case PLAYER_LIST:
                    return 6;
                case GET_SERVERS:
                    return 7;
                case MESSAGE:
                    return 8;
                case GET_SERVER:
                    return 9;
                case KICK_PLAYER:
                    return 10;
                default:
                    throw new RuntimeException("invalid command enum!");
            }
        }

    }

}
