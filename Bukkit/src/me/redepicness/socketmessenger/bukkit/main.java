package me.redepicness.socketmessenger.bukkit;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class main extends JavaPlugin{

    @Override
    public void onEnable() {
        int port = -1;
        try {
            if(!getDataFolder().exists()) {
                if(!getDataFolder().mkdir()){
                    throw new RuntimeException("Could not create configuration folder!");
                }
            }
            File config = new File(getDataFolder(), "socketMessenger.yml");

            if(!config.exists()){
                if(!config.createNewFile()){
                    throw new RuntimeException("Could not create configuration file!");
                }
                YamlConfiguration conf = YamlConfiguration.loadConfiguration(config);
                conf.set("port", 55555);
                conf.save(config);
                port = 55555;
            }
            else{
                port = YamlConfiguration.loadConfiguration(config).getInt("port");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        SocketManager.init(port);
    }

    @Override
    public void onDisable(){
        SocketManager.sendCommand(SocketManager.Command.EXIT);
    }

}
