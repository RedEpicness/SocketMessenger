package me.redepicness.socketmessenger.bukkit;

import org.bukkit.plugin.java.JavaPlugin;

public class main extends JavaPlugin{

    @Override
    public void onEnable() {
        SocketManager.init();
    }

    @Override
    public void onDisable(){
        SocketManager.end();
    }

}
