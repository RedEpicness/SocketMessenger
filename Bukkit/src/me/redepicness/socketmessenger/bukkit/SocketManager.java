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
            e.printStackTrace();
        }
    }

    static void end(boolean shutdown){
        try {
            if(!socket.isClosed()) socket.close();
            Util.log("Socket closed!");
            if(!shutdown){
                Util.log("Trying to reconnect in 5 seconds!");
                Bukkit.getScheduler().scheduleSyncDelayedTask(Bukkit.getPluginManager().getPlugin("SocketMessenger"), () -> init(port));
            }
        } catch (IOException e) {
            if(e.getMessage().toLowerCase().contains("connection refused")){
                Util.log("CANNOT CONNECT TO PORT "+port+"! CONNECTION REFUSED!");
                return;
            }
            e.printStackTrace();
        }
    }

    private static void initSocket(Socket s){
        try {
            socket = s;
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
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

    private static void sendCommand(Command command, Object... data){
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
                default:
                    throw new RuntimeException("Unknown command!");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
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

