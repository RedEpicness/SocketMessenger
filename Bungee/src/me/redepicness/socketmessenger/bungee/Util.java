package me.redepicness.socketmessenger.bungee;

import net.md_5.bungee.BungeeCord;

import java.util.logging.Level;

public class Util {

    public static void log(String message){
        BungeeCord.getInstance().getLogger().log(Level.INFO, message);
    }

}
