package me.thelennylord.ProximityChat;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class Main extends JavaPlugin {

    private File configFile;
    private FileConfiguration config;

    @Override
    public void onEnable() {
        getServer().getConsoleSender().sendMessage("ProximityChat has been enabled.");

        ProximityChatEvent proximityChatEvent = new ProximityChatEvent();
        proximityChatEvent.setInstance(this);

        getCommand("globalchat").setExecutor(proximityChatEvent);
        getServer().getPluginManager().registerEvents(proximityChatEvent, this);

        loadConfig();
    }

    @Override
    public void onDisable() {
        getServer().getConsoleSender().sendMessage("ProximityChat has been disabled.");
    }

    private void loadConfig() {
        getConfig().options().copyDefaults(true);
        saveConfig();
    }

}
