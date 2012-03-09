package net.betterverse.oregenchange;

import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;

public class OreGenChange extends JavaPlugin {

    public ArrayList<String> worlds;
    public HashMap<String, Double> settings = new HashMap<String, Double>();

    public void onDisable() {
    }

    public void onEnable() {
        Config config = new Config(this);
        settings = config.init();
        worlds = config.worlds;
        ChunkListener listener = new ChunkListener(this);
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(listener, this);
    }
}

