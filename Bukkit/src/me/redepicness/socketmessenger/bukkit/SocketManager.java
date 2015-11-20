package me.redepicness.socketmessenger.bukkit;

import me.redepicness.socketmessenger.api.Data;
import me.redepicness.socketmessenger.api.ReceivedDataEvent;
import org.bukkit.Bukkit;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Inet4Address;
import java.net.Socket;

class SocketManager {

    private static Socket socket;
    private static boolean valid = false;
    private static ObjectOutputStream out;
    private static ObjectInputStream in;
    private static int port = -1;

    static void init(int p){
        port = p;
        try {
            Util.log("Trying to connect on port "+port+"!");
            Socket socket = new Socket(Inet4Address.getLocalHost(), port);
            Thread thread = new Thread(() -> {
                initSocket(socket);
            });
            thread.start();
        } catch (IOException e) {
            if(e.getMessage().toLowerCase().contains("connection refused")){
                Util.log("CANNOT CONNECT TO PORT "+port+"! CONNECTION REFUSED!");
                Util.log("Trying to reconnect in 5 seconds!");
                Bukkit.getScheduler().scheduleSyncDelayedTask(Bukkit.getPluginManager().getPlugin("SocketMessenger"), () -> init(port), 5*20);
                return;
            }
            e.printStackTrace();
        }
    }

    static void end(boolean shutdown){
        try {
            if(!socket.isClosed()) socket.close();
            valid = false;
            Util.log("Socket closed!");
            if(!shutdown){
                Util.log("Trying to reconnect in 5 seconds!");
                Bukkit.getScheduler().scheduleSyncDelayedTask(Bukkit.getPluginManager().getPlugin("SocketMessenger"), () -> init(port), 5*20);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void initSocket(Socket s){
        try {
            socket = s;
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
            valid = true;
            sendCommand(Command.IDENTIFY);
            while(!socket.isClosed()){
                if(in.available() <= 0) continue;
                Command command = Command.get(in.readByte());
                switch(command){
                    case EXIT:
                        Util.log("Server sent end command!");
                        end(false);
                        break;
                    case SEND_DATA:
                        String channel = in.readUTF();
                        Data data = ((Data) in.readObject());
                        Util.log("Received data from Server! Channel: '"+channel+"'!");
                        Bukkit.getPluginManager().callEvent(new ReceivedDataEvent(data, channel));
                        break;
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    static void sendCommand(Command command, Object... data){
        if(!valid) return;
        try {
            switch(command) {
                case EXIT:
                    out.writeByte(command.getByte());
                    out.flush();
                    end(false);
                    break;
                case IDENTIFY:
                    Util.log("Identifying to server with port "+Bukkit.getPort()+"!");
                    out.writeByte(command.getByte());
                    out.writeInt(Bukkit.getPort());
                    out.flush();
                    break;
                case BROADCAST:
                    if(data.length < 1) throw new RuntimeException("Not enough data provided for BROADCAST command!");
                    if(!(data[0] instanceof String)) throw new RuntimeException("1st object for BROADCAST is not of type String!");
                    String msg = ((String) data[0]);
                    out.writeByte(command.getByte());
                    out.writeUTF(msg);
                    out.flush();
                    break;
                case SEND_DATA:
                    if(data.length < 2) throw new RuntimeException("Not enough data provided for SEND_DATA command!");
                    if(!(data[0] instanceof String)) throw new RuntimeException("1st object for SEND_DATA is not of type String!");
                    if(!(data[1] instanceof Data)) throw new RuntimeException("2nd object for SEND_DATA is not of type Data!");
                    String channel = ((String) data[0]);
                    Data d = ((Data) data[1]);
                    out.writeByte(command.getByte());
                    out.writeUTF(channel);
                    out.writeObject(d);
                    out.flush();
                    break;
                case FORWARD_DATA:
                    if(data.length < 2) throw new RuntimeException("Not enough data provided for FORWARD_DATA command!");
                    if(!(data[0] instanceof String)) throw new RuntimeException("1st object for FORWARD_DATA is not of type String!");
                    if(!(data[1] instanceof Data)) throw new RuntimeException("2nd object for FORWARD_DATA is not of type Data!");
                    String serv = ((String) data[0]);
                    Data dat = ((Data) data[1]);
                    out.writeByte(command.getByte());
                    out.writeUTF(serv);
                    out.writeObject(dat);
                    out.flush();
                    break;
                case CONNECT:
                    if(data.length < 1) throw new RuntimeException("Not enough data provided for CONNECT command!");
                    if(!(data[0] instanceof String)) throw new RuntimeException("1st object for CONNECT is not of type String!");
                    if(!(data[1] instanceof String)) throw new RuntimeException("2nd object for CONNECT is not of type String!");
                    String p = ((String) data[0]);
                    String server = ((String) data[1]);
                    out.writeByte(command.getByte());
                    out.writeUTF(p);
                    out.writeUTF(server);
                    out.flush();
                    break;
                case MESSAGE:
                    if(data.length < 1) throw new RuntimeException("Not enough data provided for MESSAGE command!");
                    if(!(data[0] instanceof String)) throw new RuntimeException("1st object for MESSAGE is not of type String!");
                    if(!(data[1] instanceof String)) throw new RuntimeException("2nd object for MESSAGE is not of type String!");
                    String pl = ((String) data[0]);
                    String m = ((String) data[1]);
                    out.writeByte(command.getByte());
                    out.writeUTF(pl);
                    out.writeUTF(m);
                    out.flush();
                    break;
                case KICK_PLAYER:
                    if(data.length < 1) throw new RuntimeException("Not enough data provided for KICK_PLAYER command!");
                    if(!(data[0] instanceof String)) throw new RuntimeException("1st object for KICK_PLAYER is not of type String!");
                    if(!(data[1] instanceof String)) throw new RuntimeException("2nd object for KICK_PLAYER is not of type String!");
                    String player = ((String) data[0]);
                    String message = ((String) data[1]);
                    out.writeByte(command.getByte());
                    out.writeUTF(player);
                    out.writeUTF(message);
                    out.flush();
                    break;
                case GET_SERVER:
                case GET_SERVERS:
                case PLAYER_COUNT:
                case PLAYER_LIST:
                    out.writeByte(command.getByte());
                    out.flush();
                    break;
                default:
                    throw new RuntimeException("Unknown command!");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
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

