package me.redepicness.socketmessenger.bungee.bungee;

import java.util.HashMap;

public class Data {

    private HashMap<String, Object> map = new HashMap<>();

    /*
        Setters
     */

    public void addString(String id, String data){
        map.put(id, data);
    }

    public void addInt(String id, int data){
        map.put(id, data);
    }

    public void addDouble(String id, double data){
        map.put(id, data);
    }

    public void addFloat(String id, float data){
        map.put(id, data);
    }

    public void addBoolean(String id, boolean data){
        map.put(id, data);
    }

    public void addLong(String id, long data){
        map.put(id, data);
    }

    public void addByte(String id, byte data){
        map.put(id, data);
    }

    public void addShort(String id, short data){
        map.put(id, data);
    }

}
