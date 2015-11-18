package me.redepicness.socketmessenger.bungee;

import net.md_5.bungee.BungeeCord;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;

class SocketManager {

    static HashMap<String, SocketClient> connectedSockets = new HashMap<>();
    static int ID = 0;
    private static ServerSocket serverSocket;

    static void init(int port){
        try {
            Util.log("Listening for socket connections on port "+port+"!");
            serverSocket = new ServerSocket(port);
            ExecutorService service = BungeeCord.getInstance().getPluginManager().getPlugin("SocketMessenger").getExecutorService();
            service.submit(() -> {
                while(!serverSocket.isClosed()){
                    try {
                        Socket socket = serverSocket.accept();
                        service.submit(() -> initSocket(socket));
                    } catch (IOException e) {
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
            connectedSockets.values().forEach(SocketClient::end);
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

        EXIT, IDENTIFY, BROADCAST, SEND_DATA;

        public static Command get(byte command){
            switch(command){
                case 0:
                    return EXIT;
                case 1:
                    return BROADCAST;
                case 2:
                    return SEND_DATA;
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
                default:
                    throw new RuntimeException("invalid command enum!");
            }
        }

    }

}
