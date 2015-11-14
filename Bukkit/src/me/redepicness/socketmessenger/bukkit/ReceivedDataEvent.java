package me.redepicness.socketmessenger.bukkit;

import me.redepicness.socketmessenger.Data;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ReceivedDataEvent extends Event{

    private static final HandlerList handlers = new HandlerList();

    private Data data;
    private String channel;

    public ReceivedDataEvent(Data data, String channel) {
        this.data = data;
        this.channel = channel;
    }

    public Data getData() {
        return data;
    }

    public String getChannel() {
        return channel;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

}
