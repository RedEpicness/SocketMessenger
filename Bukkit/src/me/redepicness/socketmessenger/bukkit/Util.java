package me.redepicness.socketmessenger.bukkit;

import org.bukkit.Bukkit;

import java.util.logging.Level;

public class Util {

    public static void log(String message){
        Bukkit.getLogger().log(Level.INFO, message);
    }

}
