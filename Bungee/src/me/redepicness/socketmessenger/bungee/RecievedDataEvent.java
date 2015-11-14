package me.redepicness.socketmessenger.bungee;

import me.redepicness.socketmessenger.Data;
import net.md_5.bungee.api.plugin.Event;

public class RecievedDataEvent extends Event{

    private Data data;
    private String channel;

    public RecievedDataEvent(Data data, String channel) {
        this.data = data;
        this.channel = channel;
    }

    public Data getData() {
        return data;
    }

    public String getChannel() {
        return channel;
    }
}
