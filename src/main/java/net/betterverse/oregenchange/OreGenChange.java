package net.betterverse.oregenchange;

import org.bukkit.World;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;

public class OreGenChange extends JavaPlugin {

    public ArrayList<World> worldEnabled = new ArrayList<World>();
    public HashMap<World, HashMap<String, Double>> worlds = new HashMap<World, HashMap<String, Double>>();

    public void onDisable() {
    }

    public void onEnable() {
        for (World world : this.getServer().getWorlds()) {
            Config config = new Config(this, world);
            HashMap<String, Double> settings = config.init();
            worlds.put(world, settings);
        }

        ChunkListener listener = new ChunkListener(this);
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(listener, this);
        System.out.println("durrrr");
    }
}

