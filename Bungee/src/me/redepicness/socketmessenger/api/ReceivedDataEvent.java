package me.redepicness.socketmessenger.api;

import net.md_5.bungee.api.plugin.Event;

/**
 * Called when Data is received on a specific channel trough the socket.
 */
public class ReceivedDataEvent extends Event{

    private final Data data;
    private final String sender;
    private final String channel;

    public ReceivedDataEvent(Data data, String sender, String channel) {
        this.data = data;
        this.sender = sender;
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
     *
     * @return The channel which Data was received trough.
     */
    public String getChannel() {
        return channel;
    }

    /**
     * Gets the sender which sent the Data.
     *
     * @return The sender which sent the Data.
     */
    public String getSender() {
        return sender;
    }
}
