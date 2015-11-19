package me.redepicness.socketmessenger.api;

import me.redepicness.socketmessenger.api.Data;
import net.md_5.bungee.api.plugin.Event;

/**
 * Called when Data is received on a specific channel trough the socket.
 */
public class ReceivedDataEvent extends Event{

    private final Data data;
    private final String channel;

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
