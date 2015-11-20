package me.redepicness.socketmessenger.bukkit;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class main extends JavaPlugin{

    @Override
    public void onEnable() {
        int port = -1;
        String address = "localhost";
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
                conf.set("address", "localhost");
                conf.save(config);
                port = 55555;
            }
            else{
                port = YamlConfiguration.loadConfiguration(config).getInt("port");
                address = YamlConfiguration.loadConfiguration(config).getString("address");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        SocketManager.init(address, port);
    }

    @Override
    public void onDisable(){
        SocketManager.sendCommand(SocketManager.Command.EXIT);
    }

}
