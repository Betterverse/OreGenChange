package net.betterverse.oregenchange;

import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.logging.Logger;

public class OreGenChange extends JavaPlugin {
    private static final Logger log = Logger.getLogger("Minecraft");
    private static String PREFIX;
    private static boolean DEBUG_ENABLED = false;
    private static OreGenChange instance;
    private Config config;
    public HashMap<World, HashMap<String, Double>> worlds = new HashMap<World, HashMap<String, Double>>();

    public void onDisable() {
    }

    public void onEnable() {
        PREFIX = "[" + getDescription().getName() + "] ";
        instance = this;
        config = new Config(this);
        getServer().getPluginManager().registerEvents(new ChunkListener(this), this);
    }

    public static OreGenChange getInstance() {
        return instance;
    }

    public Config getCustomConfig() {
        return this.config;
    }

    public static void info(String msg) {
        log.info(PREFIX + msg);
    }

    public static void debug(String msg) {
        if(DEBUG_ENABLED) info(msg);
    }

    public static void warning(String msg) {
        log.warning(PREFIX + msg);
    }
}

