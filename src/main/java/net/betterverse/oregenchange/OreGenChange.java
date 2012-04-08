package net.betterverse.oregenchange;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public class OreGenChange extends JavaPlugin {
    private static final Logger log = Logger.getLogger("Minecraft");
    private static String PREFIX;
    private static OreGenChange instance;
    private static Config config;

    public void onDisable() {
    }

    public void onEnable() {
        PREFIX = "[" + getDescription().getName() + "] ";
        instance = this;
        config = new Config(this);
        getServer().getPluginManager().registerEvents(new ChunkListener(this), this);
        debug("Debug is enabled!");
    }

    public static OreGenChange getInstance() {
        return instance;
    }

    public Config getCustomConfig() {
        return config;
    }

    public static void info(String msg) {
        log.info(PREFIX + msg);
    }

    public static void debug(String msg) {
        if(instance.getConfig().getBoolean("worlds.debug", false)) {
            info(msg);
        }
    }

    public static void warning(String msg) {
        log.warning(PREFIX + msg);
    }
}

