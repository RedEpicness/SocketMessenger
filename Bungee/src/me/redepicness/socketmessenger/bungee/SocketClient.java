package me.redepicness.socketmessenger.bungee;

import me.redepicness.socketmessenger.api.Data;
import me.redepicness.socketmessenger.api.ReceivedDataEvent;
import me.redepicness.socketmessenger.bungee.SocketManager.Command;
import net.md_5.bungee.BungeeCord;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

class SocketClient {

    private final Socket socket;
    private String name = "";
    private final int id;
    private int serverPort;
    private boolean identified = false;
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
        try {
            while(socket.isConnected()){
                if(in.available() <= 0) continue;
                Command command = Command.get(in.readByte());
                switch(command){
                    case EXIT:
                        Util.log(fullName()+" sent end command!");
                        end();
                        break;
                    case IDENTIFY:
                        serverPort = in.readInt();
                        BungeeCord.getInstance().getServers().values().stream()
                                .filter(info -> info.getAddress().getPort() == serverPort)
                                .forEach(info -> name = info.getName());
                        if(name.equals("")){
                            name = "Undefined";
                            Util.log("Socked "+fullName()+" does not originate from any registered server! Disconnecting!");
                            end();
                            return;
                        }
                        Util.log("Socket "+id+"'s origin is '"+name+"'!");
                        if(SocketManager.connectedSockets.containsKey(name)){
                            Util.log("An open socket from "+name+" already exists! Disconnecting "+fullName()+"!");
                            end();
                            return;
                        }
                        identified = true;
                        SocketManager.connectedSockets.put(name, this);
                        sendCommand(Command.IDENTIFY);
                        break;
                    case BROADCAST:
                        if(!identified) return;
                        String message = in.readUTF();
                        Util.log("Incoming broadcast from "+fullName()+"! '"+message+"'!");
                        BungeeCord.getInstance().broadcast(message);
                        break;
                    case SEND_DATA:
                        if(!identified) return;
                        String channel = in.readUTF();
                        Data data = ((Data) in.readObject());
                        Util.log("Received data from "+fullName()+"! Channel: '"+channel+"'!");
                        BungeeCord.getInstance().getPluginManager().callEvent(new ReceivedDataEvent(data, channel));
                        break;
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        if(!socket.isConnected()) end();
    }

    void end(){
        try {
            if(!socket.isClosed()) socket.close();
            SocketManager.connectedSockets.remove(name);
            Util.log(fullName()+" closed!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendCommand(Command command, Object... data){
        if(!identified) return;
        try {
            switch(command) {
                case EXIT:
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
