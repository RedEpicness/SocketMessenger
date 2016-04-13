package me.redepicness.socketmessenger.bungee;

import net.md_5.bungee.BungeeCord;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
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

        EXIT(0),
        IDENTIFY(127),
        BROADCAST(1),
        SEND_DATA(2),
        FORWARD_DATA(3),
        CONNECT(4),
        PLAYER_COUNT(5),
        PLAYER_LIST(6),
        GET_SERVERS(7),
        MESSAGE(8),
        GET_SERVER(9),
        KICK_PLAYER(10);

        private static HashMap<Byte, Command> BYTE_TO_COMMAND = new HashMap<>();

        private byte id;

        Command(int id){
            this.id = (byte)id;
            registerID();
        }

        private void registerID(){
            BYTE_TO_COMMAND.put(id, this);
        }

        public static Command get(byte command){
            return BYTE_TO_COMMAND.getOrDefault(command, null);
        }

        public byte getByte(){
            return id;
        }

    }

}
