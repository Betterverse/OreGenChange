package net.betterverse.oregenchange;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkPopulateEvent;

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
    private Material gold = Material.GOLD_ORE;
    private Material diamond = Material.DIAMOND_ORE;
    private Material iron = Material.IRON_ORE;
    private Material coal = Material.COAL_ORE;
    private Material lapis = Material.LAPIS_ORE;
    private Material redstone = Material.REDSTONE_ORE;
    private OreGenChange plugin;
    private double chance;
    private int passes;
    private int maxSize;
    private int minSize;
    private int maxHeight;

    public ChunkListener(OreGenChange plugin) {
        this.plugin = plugin;
        ironChance = plugin.settings.get("ironChance");
        ironPasses = plugin.settings.get("ironPasses");
        ironSizeMax = plugin.settings.get("ironSizeMax");
        ironSizeMin = plugin.settings.get("ironSizeMin");
        ironMaxHeight = plugin.settings.get("ironMaxHeight");
        diamondChance = plugin.settings.get("diamondChance");
        diamondPasses = plugin.settings.get("diamondPasses");
        diamondSizeMax = plugin.settings.get("diamondSizeMax");
        diamondSizeMin = plugin.settings.get("diamondSizeMin");
        diamondMaxHeight = plugin.settings.get("diamondMaxHeight");
        coalChance = plugin.settings.get("coalChance");
        coalPasses = plugin.settings.get("coalPasses");
        coalSizeMax = plugin.settings.get("coalSizeMax");
        coalSizeMin = plugin.settings.get("coalSizeMin");
        coalMaxHeight = plugin.settings.get("coalMaxHeight");
        lapisChance = plugin.settings.get("lapisChance");
        lapisPasses = plugin.settings.get("lapisPasses");
        lapisSizeMax = plugin.settings.get("lapisSizeMax");
        lapisSizeMin = plugin.settings.get("lapisSizeMin");
        lapisMaxHeight = plugin.settings.get("lapisMaxHeight");
        redstoneChance = plugin.settings.get("redstoneChance");
        redstonePasses = plugin.settings.get("redstonePasses");
        redstoneSizeMax = plugin.settings.get("redstoneSizeMax");
        redstoneSizeMin = plugin.settings.get("redstoneSizeMin");
        redstoneMaxHeight = plugin.settings.get("redstoneMaxHeight");
        goldChance = plugin.settings.get("goldChance");
        goldPasses = plugin.settings.get("goldPasses");
        goldSizeMax = plugin.settings.get("goldSizeMax");
        goldSizeMin = plugin.settings.get("goldSizeMin");
        goldMaxHeight = plugin.settings.get("goldMaxHeight");
    }

    @EventHandler
    public void onChunkPopulate(ChunkPopulateEvent event) {

        if (plugin.worlds.contains(event.getWorld().getName())) {
            Chunk chunk = event.getChunk();
            // Removes original ores, don't think there's a faster way of doing this :c
            for (int x = 0; x < 16; x++) {
                for (int z = 0; z < 16; z++) {
                    for (int y = 0; y < 90; y++) {
                        Material block = chunk.getBlock(x, y, z).getType();
                        if (block == gold || block == diamond || block == iron || block == coal || block == lapis || block == redstone) {
                            Block b = chunk.getBlock(x, y, z);
                            b.setType(stone);
                        }
                    }
                }
            }

            Material[] ores = {gold, diamond, iron, coal, lapis, redstone};
            for (int i = 0; i < ores.length; i++) {
                // there's probably an easier way of doing this but i dont care
                if (i == 0) {
                    chance = goldChance;
                    passes = (int) goldPasses;
                    maxSize = (int) goldSizeMax;
                    minSize = (int) goldSizeMin;
                    maxHeight = (int) goldMaxHeight;
                } else if (i == 1) {
                    chance = diamondChance;
                    passes = (int) diamondPasses;
                    maxSize = (int) diamondSizeMax;
                    minSize = (int) diamondSizeMin;
                    maxHeight = (int) diamondMaxHeight;
                } else if (i == 2) {
                    chance = ironChance;
                    passes = (int) ironPasses;
                    maxSize = (int) ironSizeMax;
                    minSize = (int) ironSizeMin;
                    maxHeight = (int) ironMaxHeight;
                } else if (i == 3) {
                    chance = coalChance;
                    passes = (int) coalPasses;
                    maxSize = (int) coalSizeMax;
                    minSize = (int) coalSizeMin;
                    maxHeight = (int) coalMaxHeight;
                } else if (i == 4) {
                    chance = (int) lapisChance;
                    passes = (int) lapisPasses;
                    maxSize = (int) lapisSizeMax;
                    minSize = (int) lapisSizeMin;
                    maxHeight = (int) lapisMaxHeight;
                } else if (i == 5) {
                    chance = redstoneChance;
                    passes = (int) redstonePasses;
                    maxSize = (int) redstoneSizeMax;
                    minSize = (int) redstoneSizeMin;
                    maxHeight = (int) redstoneMaxHeight;
                }

                for (int l = 0; l < passes; l++) {
                    if ((chance) >= Math.random()) {
                        Random r = new Random();
                        int x = r.nextInt(16);
                        int z = r.nextInt(16);
                        int y = r.nextInt(maxHeight - 4) + 4;
                        snake(x, y, z, minSize, maxSize, r, ores[i], chunk);
                    }
                }
            }
        }
    }

    private void snake(int x, int y, int z, int minSize, int maxSize, Random r, Material ore, Chunk chunk) {
        boolean dir = r.nextBoolean();
        deposit(x, y, z, ore, chunk, r, dir);
        int move = 0;
        int shiftVer = 0;
        int shiftHor = 0;
        for (int v = 0; v < (maxSize - minSize); v++) {
            move++;
            int rand = r.nextInt(6);
            if (rand == 1) shiftHor++;
            else if (rand == 2) shiftHor--;
            else if (rand == 3) shiftVer++;
            else if (rand == 4) shiftVer--;
            if (dir) deposit(x + shiftHor, y + shiftVer, z + move, ore, chunk, r, dir);
            else deposit(x + move, y + shiftVer, z + shiftHor, ore, chunk, r, dir);
        }
    }

    private void deposit(int x, int y, int z, Material ore, Chunk chunk, Random r, boolean dir) {
        for (int h1 = 0; h1 < 3; h1++) {
            for (int v1 = 0; v1 < 2; v1++) {
                Block b;
                if (dir)
                    b = chunk.getBlock(x - h1, y - v1, z + h1);
                else
                    b = chunk.getBlock(x, y - v1, z - h1);
                if (b.getType() == stone && r.nextInt(8) != 1)
                    b.setType(ore);
            }
        }
    }
}
