package net.betterverse.oregenchange;

import org.bukkit.Chunk;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkPopulateEvent;

public class ChunkListener implements Listener {
    private OreGenChange plugin;

    public ChunkListener(OreGenChange plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onChunkPopulate(ChunkPopulateEvent event) {
        String world = event.getWorld().getName();
        if (plugin.getCustomConfig().isWatchedWorld(world)) {
            OreGenChange.debug("World is enabled for chunking! " + world);
            Chunk chunk = event.getChunk();
            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new ChunkThread(chunk));
        } else {
            OreGenChange.debug("World is not enabled for chunking: " + world);
        }
    }
}
