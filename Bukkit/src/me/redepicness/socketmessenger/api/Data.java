package me.redepicness.socketmessenger.api;

import java.io.Serializable;
import java.util.HashMap;

/**
 *  This class is used to transmit data trough the socket.
 */
public class Data implements Serializable{

    private static final long serialVersionUID = 1;

    private HashMap<String, DataObject> map = new HashMap<>();

    /**
     * Adds a String to the map of objects to send.
     *
     * @param id - The id to save this String as.
     * @param data - The String to save.
     */
    public void addString(String id, String data){
        map.put(id, new DataObject(DataType.STRING, data));
    }

    /**
     * Adds a int to the map of objects to send.
     *
     * @param id - The id to save this int as.
     * @param data - The int to save.
     */
    public void addInt(String id, int data){
        map.put(id, new DataObject(DataType.INT, data));
    }

    /**
     * Adds a double to the map of objects to send.
     *
     * @param id - The id to save this double as.
     * @param data - The double to save.
     */
    public void addDouble(String id, double data){
        map.put(id, new DataObject(DataType.DOUBLE, data));
    }

    /**
     * Adds a float to the map of objects to send.
     *
     * @param id - The id to save this float as.
     * @param data - The float to save.
     */
    public void addFloat(String id, float data){
        map.put(id, new DataObject(DataType.FLOAT, data));
    }

    /**
     * Adds a boolean to the map of objects to send.
     *
     * @param id - The id to save this boolean as.
     * @param data - The boolean to save.
     */
    public void addBoolean(String id, boolean data){
        map.put(id, new DataObject(DataType.BOOLEAN, data));
    }

    /**
     * Adds a long to the map of objects to send.
     *
     * @param id - The id to save this long as.
     * @param data - The long to save.
     */
    public void addLong(String id, long data){
        map.put(id, new DataObject(DataType.LONG, data));
    }

    /**
     * Adds a byte to the map of objects to send.
     *
     * @param id - The id to save this byte as.
     * @param data - The byte to save.
     */
    public void addByte(String id, byte data){
        map.put(id, new DataObject(DataType.BYTE, data));
    }

    /**
     * Adds a short to the map of objects to send.
     *
     * @param id - The id to save this short as.
     * @param data - The short to save.
     */
    public void addShort(String id, short data){
        map.put(id, new DataObject(DataType.SHORT, data));
    }

    /**
     * Adds an Object to the map of objects to send.
     * WARNING: The object must implement the interface java.io.Serializable!
     *
     * @param id - The id to save this Object as.
     * @param data - The Object to save.
     */
    public void addObject(String id, Object data){
        map.put(id, new DataObject(DataType.OBJECT, data));
    }

    /**
     * Get the String specified under id.
     *
     * @param id - The id of the string to get.
     * @return - The String found under id.
     */
    public String getString(String id){
        DataObject object = map.get(id);
        if(object == null) throw new RuntimeException("No object with id '"+id+"' found!");
        if(!object.getType().equals(DataType.STRING)) throw new RuntimeException("Object id " + id + " is not a String DataObject!");
        return ((String) object.getData());
    }

    /**
     * Get the int specified under id.
     *
     * @param id - The id of the int to get.
     * @return - The int found under id.
     */
    public int getInt(String id){
        DataObject object = map.get(id);
        if(object == null) throw new RuntimeException("No object with id '"+id+"' found!");
        if(!object.getType().equals(DataType.INT)) throw new RuntimeException("Type and data do not match in DataObject!");
        return ((int) object.getData());
    }

    /**
     * Get the double specified under id.
     *
     * @param id - The id of the double to get.
     * @return - The double found under id.
     */
    public double getDouble(String id){
        DataObject object = map.get(id);
        if(object == null) throw new RuntimeException("No object with id '"+id+"' found!");
        if(!object.getType().equals(DataType.DOUBLE)) throw new RuntimeException("Type and data do not match in DataObject!");
        return ((double) object.getData());
    }

    /**
     * Get the float specified under id.
     *
     * @param id - The id of the float to get.
     * @return - The float found under id.
     */
    public float getFloat(String id){
        DataObject object = map.get(id);
        if(object == null) throw new RuntimeException("No object with id '"+id+"' found!");
        if(!object.getType().equals(DataType.FLOAT)) throw new RuntimeException("Type and data do not match in DataObject!");
        return ((float) object.getData());
    }

    /**
     * Get the boolean specified under id.
     *
     * @param id - The id of the boolean to get.
     * @return - The boolean found under id.
     */
    public boolean getBoolean(String id){
        DataObject object = map.get(id);
        if(object == null) throw new RuntimeException("No object with id '"+id+"' found!");
        if(!object.getType().equals(DataType.BOOLEAN)) throw new RuntimeException("Type and data do not match in DataObject!");
        return ((boolean) object.getData());
    }

    /**
     * Get the long specified under id.
     *
     * @param id - The id of the long to get.
     * @return - The long found under id.
     */
    public long getLong(String id){
        DataObject object = map.get(id);
        if(object == null) throw new RuntimeException("No object with id '"+id+"' found!");
        if(!object.getType().equals(DataType.LONG)) throw new RuntimeException("Type and data do not match in DataObject!");
        return ((long) object.getData());
    }

    /**
     * Get the byte specified under id.
     *
     * @param id - The id of the byte to get.
     * @return - The byte found under id.
     */
    public byte getByte(String id){
        DataObject object = map.get(id);
        if(object == null) throw new RuntimeException("No object with id '"+id+"' found!");
        if(!object.getType().equals(DataType.BYTE)) throw new RuntimeException("Type and data do not match in DataObject!");
        return ((byte) object.getData());
    }

    /**
     * Get the short specified under id.
     *
     * @param id - The id of the short to get.
     * @return - The short found under id.
     */
    public short getShort(String id){
        DataObject object = map.get(id);
        if(object == null) throw new RuntimeException("No object with id '"+id+"' found!");
        if(!object.getType().equals(DataType.SHORT)) throw new RuntimeException("Type and data do not match in DataObject!");
        return ((short) object.getData());
    }

    /**
     * Get the Object specified under id.
     *
     * @param id - The id of the Object to get.
     * @return - The Object found under id.
     */
    public <T> T getObject(String id){
        DataObject object = map.get(id);
        if(object == null) throw new RuntimeException("No object with id '"+id+"' found!");
        //noinspection unchecked
        return (T) object.getData();
    }

    private class DataObject implements Serializable {

        private DataType type;
        private Object data;

        private DataObject(DataType type, Object data) {
            this.type = type;
            this.data = data;
        }

        private DataType getType(){
            return type;
        }

        private Object getData(){
            return data;
        }

    }

    private enum DataType implements Serializable{

        BYTE, SHORT, INT, LONG, DOUBLE, FLOAT, BOOLEAN, OBJECT, STRING

    }

}
