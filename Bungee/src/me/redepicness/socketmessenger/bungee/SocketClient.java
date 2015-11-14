package me.redepicness.socketmessenger.bungee;

import me.redepicness.socketmessenger.Data;
import me.redepicness.socketmessenger.bungee.SocketManager.Command;
import net.md_5.bungee.BungeeCord;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

class SocketClient {

    private Socket socket;
    private String name = "";
    private int id;
    private ObjectInputStream in;
    private ObjectOutputStream out;

    SocketClient(Socket socket) {
        this.socket = socket;
        this.id = SocketManager.ID;
        try {
            this.in = new ObjectInputStream(socket.getInputStream());
            this.out = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        dataReceiveListener();
    }

    private void dataReceiveListener(){
        while(socket.isConnected()){
            try {
                Command command = Command.get(in.readByte());
                switch(command){
                    case EXIT:
                        Util.log(fullName()+" sent end command!");
                        end();
                        break;
                    case IDENTIFY:
                        name = in.readUTF();
                        if(SocketManager.connectedSockets.containsKey(name)) name += "-"+id;
                        Util.log("Socket "+id+" identified itself as "+name+"!");
                        SocketManager.connectedSockets.put(name, this);
                        sendCommand(Command.IDENTIFY);
                        break;
                    case BROADCAST:
                        String message = in.readUTF();
                        Util.log("Incoming broadcast from "+fullName()+"! '"+message+"'!");
                        BungeeCord.getInstance().broadcast(message);
                        break;
                    case SEND_DATA:
                        try {
                            String channel = in.readUTF();
                            Data data = ((Data) in.readObject());
                            Util.log("Recieved data from "+fullName()+"! Channel: '"+channel+"'!");
                            BungeeCord.getInstance().getPluginManager().callEvent(new RecievedDataEvent(data, channel));
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                        break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if(!socket.isConnected()) end();
    }

    void end(){
        try {
            if(!socket.isClosed()) socket.close();
            out.close();
            in.close();
            SocketManager.connectedSockets.remove(name);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void sendCommand(Command command, Object... data){
        try {
            switch(command) {
                case EXIT:
                case IDENTIFY:
                    out.writeByte(command.getByte());
                    out.flush();
                    end();
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

    private String fullName(){
        return id+":"+name;
    }

}
