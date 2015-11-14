package me.redepicness.socketmessenger.bungee;

import net.md_5.bungee.api.plugin.Plugin;

public class main extends Plugin {

    @Override
    public void onEnable() {
        SocketManager.init();
    }

    @Override
    public void onDisable() {
        SocketManager.end();
    }
}
