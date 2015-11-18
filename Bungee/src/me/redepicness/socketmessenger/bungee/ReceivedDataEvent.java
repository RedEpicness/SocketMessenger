package me.redepicness.socketmessenger.bungee;

import me.redepicness.socketmessenger.Data;
import net.md_5.bungee.api.plugin.Event;

/**
 * Called when Data is received on a specific channel trough the socket.
 */
public class ReceivedDataEvent extends Event{

    private Data data;
    private String channel;

    public ReceivedDataEvent(Data data, String channel) {
        this.data = data;
        this.channel = channel;
    }

    /**
     * Gets the Data received.
     *
     * @return The Data received.
     */
    public Data getData() {
        return data;
    }

    /**
     * Gets the channel trough which Data was received.
     * @return The channel which Data was received trough.
     */
    public String getChannel() {
        return channel;
    }

}
