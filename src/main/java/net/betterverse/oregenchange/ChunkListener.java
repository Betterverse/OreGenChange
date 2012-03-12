package net.betterverse.oregenchange;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkPopulateEvent;

import java.util.HashMap;
import java.util.Random;

public class ChunkListener implements Listener {

    private double ironChance;
    private double ironPasses;
    private double ironSizeMax;
    private double ironSizeMin;
    private double ironMaxHeight;
    private double diamondChance;
    private double diamondPasses;
    private double diamondSizeMax;
    private double diamondSizeMin;
    private double diamondMaxHeight;
    private double coalChance;
    private double coalPasses;
    private double coalSizeMax;
    private double coalSizeMin;
    private double coalMaxHeight;
    private double lapisChance;
    private double lapisPasses;
    private double lapisSizeMax;
    private double lapisSizeMin;
    private double lapisMaxHeight;
    private double redstoneChance;
    private double redstonePasses;
    private double redstoneSizeMax;
    private double redstoneSizeMin;
    private double redstoneMaxHeight;
    private double goldChance;
    private double goldPasses;
    private double goldSizeMax;
    private double goldSizeMin;
    private double goldMaxHeight;
    private Material stone = Material.STONE;
    private OreGenChange plugin;
    private double chance;
    private int passes;
    private int maxSize;
    private int minSize;
    private int maxHeight;

    public ChunkListener(OreGenChange plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onChunkPopulate(ChunkPopulateEvent event) {

        if (plugin.worldEnabled.contains(event.getWorld())) {
            Chunk chunk = event.getChunk();
            // Removes original ores, don't think there's a faster way of doing this :c
            for (int x = 0; x < 16; x++) {
                for (int z = 0; z < 16; z++) {
                    for (int y = 0; y < 90; y++) {
                        Material block = chunk.getBlock(x, y, z).getType();
                        if (block == Material.GOLD_ORE || block == Material.DIAMOND_ORE || block == Material.IRON_ORE || block == Material.COAL_ORE || block == Material.LAPIS_ORE || block == Material.REDSTONE_ORE) {
                            Block b = chunk.getBlock(x, y, z);
                            b.setType(stone);
                        }
                    }
                }
            }

            HashMap<String, Double> settings = plugin.worlds.get(event.getWorld());
            ironChance = settings.get("ironChance");
            ironPasses = settings.get("ironPasses");
            ironSizeMax = settings.get("ironSizeMax");
            ironSizeMin = settings.get("ironSizeMin");
            ironMaxHeight = settings.get("ironMaxHeight");
            diamondChance = settings.get("diamondChance");
            diamondPasses = settings.get("diamondPasses");
            diamondSizeMax = settings.get("diamondSizeMax");
            diamondSizeMin = settings.get("diamondSizeMin");
            diamondMaxHeight = settings.get("diamondMaxHeight");
            coalChance = settings.get("coalChance");
            coalPasses = settings.get("coalPasses");
            coalSizeMax = settings.get("coalSizeMax");
            coalSizeMin = settings.get("coalSizeMin");
            coalMaxHeight = settings.get("coalMaxHeight");
            lapisChance = settings.get("lapisChance");
            lapisPasses = settings.get("lapisPasses");
            lapisSizeMax = settings.get("lapisSizeMax");
            lapisSizeMin = settings.get("lapisSizeMin");
            lapisMaxHeight = settings.get("lapisMaxHeight");
            redstoneChance = settings.get("redstoneChance");
            redstonePasses = settings.get("redstonePasses");
            redstoneSizeMax = settings.get("redstoneSizeMax");
            redstoneSizeMin = settings.get("redstoneSizeMin");
            redstoneMaxHeight = settings.get("redstoneMaxHeight");
            goldChance = settings.get("goldChance");
            goldPasses = settings.get("goldPasses");
            goldSizeMax = settings.get("goldSizeMax");
            goldSizeMin = settings.get("goldSizeMin");
            goldMaxHeight = settings.get("goldMaxHeight");

            int[] ores = {14, 56, 15, 16, 73, 21};
            for (int ore : ores) {
                // there's probably an easier way of doing this but i dont care
                if (ore == 14) {
                    chance = goldChance;
                    passes = (int) goldPasses;
                    maxSize = (int) goldSizeMax;
                    minSize = (int) goldSizeMin;
                    maxHeight = (int) goldMaxHeight;
                } else if (ore == 56) {
                    chance = diamondChance;
                    passes = (int) diamondPasses;
                    maxSize = (int) diamondSizeMax;
                    minSize = (int) diamondSizeMin;
                    maxHeight = (int) diamondMaxHeight;
                } else if (ore == 15) {
                    chance = ironChance;
                    passes = (int) ironPasses;
                    maxSize = (int) ironSizeMax;
                    minSize = (int) ironSizeMin;
                    maxHeight = (int) ironMaxHeight;
                } else if (ore == 16) {
                    chance = coalChance;
                    passes = (int) coalPasses;
                    maxSize = (int) coalSizeMax;
                    minSize = (int) coalSizeMin;
                    maxHeight = (int) coalMaxHeight;
                } else if (ore == 73) {
                    chance = (int) lapisChance;
                    passes = (int) lapisPasses;
                    maxSize = (int) lapisSizeMax;
                    minSize = (int) lapisSizeMin;
                    maxHeight = (int) lapisMaxHeight;
                } else if (ore == 21) {
                    chance = redstoneChance;
                    passes = (int) redstonePasses;
                    maxSize = (int) redstoneSizeMax;
                    minSize = (int) redstoneSizeMin;
                    maxHeight = (int) redstoneMaxHeight;
                } else continue;

                for (int l = 0; l < passes; l++) {
                    if ((chance) >= Math.random()) {
                        Random r = new Random();
                        int x = r.nextInt(16);
                        int z = r.nextInt(16);
                        int y = r.nextInt(maxHeight - 4) + 4;
                        snake(x, y, z, minSize, maxSize, r, ore, chunk);
                    }
                }
            }
        }
    }

    private void snake(int x, int y, int z, int minSize, int maxSize, Random r, int ore, Chunk chunk) {
        boolean dir = r.nextBoolean();
        deposit(x, y, z, ore, chunk, r, dir);
        int move = 0;
        int shiftVer = 0;
        int shiftHor = 0;
        for (int v = 0; v < (maxSize - minSize); v++) {
            move++;
            int rand = r.nextInt(7);
            if (rand == 1) shiftHor++;
            else if (rand == 2) shiftHor--;
            else if (rand == 3) shiftVer++;
            else if (rand == 4) shiftVer--;
            if (dir) deposit(x + shiftHor, y + shiftVer, z + move, ore, chunk, r, dir);
            else deposit(x + move, y + shiftVer, z + shiftHor, ore, chunk, r, dir);
        }
    }

    private void deposit(int x, int y, int z, int ore, Chunk chunk, Random r, boolean dir) {
        for (int h1 = -1; h1 < 2; h1++) {
            for (int v1 = 0; v1 < 2; v1++) {
                Block b;
                if (dir)
                    b = chunk.getBlock(x + h1, y - v1, z);
                else
                    b = chunk.getBlock(x, y - v1, z - h1);
                if (b.getType() == stone && r.nextInt(6) != 1) {
                    System.out.println("placed " + ore);
                    b.setTypeId(ore);
                }
            }
        }
    }
}
