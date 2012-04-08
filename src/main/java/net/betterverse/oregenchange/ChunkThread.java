package net.betterverse.oregenchange;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

import java.util.Random;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Collections;

public class ChunkThread implements Runnable {
    private Chunk chunk;
    private String world;
    private Map<Block, Material> blocks = new HashMap<Block, Material>();

    public ChunkThread(Chunk chunk) {
        this.chunk = chunk;
        this.world = chunk.getWorld().getName();
    }

    public void run() {
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                for (int y = 0; y < 90; y++) {
                    Block block = this.chunk.getBlock(x, y, z);
                    if (OreGenChange.getInstance().getCustomConfig().isWatchedMaterial(this.world, block.getType()) &&
                            OreGenChange.getInstance().getCustomConfig().getChance(this.world, block.getType()) != 1) {
                        this.blocks.put(block, block.getType());
                    }
                }
            }
        }
        handleBlocks();
    }

    private void handleBlocks() {
        OreGenChange.debug("Let's handle the blocks!");
        Map<Material, List<Block>> temporary = new HashMap<Material, List<Block>>();
        for (Map.Entry<Block, Material> map : this.blocks.entrySet())  {
            if (temporary.containsKey(map.getValue())) {
                List<Block> tempList = temporary.get(map.getValue());
                tempList.add(map.getKey());
                temporary.put(map.getValue(), tempList);
            } else {
                List<Block> tempList = new ArrayList<Block>();
                tempList.add(map.getKey());
                temporary.put(map.getValue(), tempList);
            }
        }
        for (Map.Entry<Material, List<Block>> map : temporary.entrySet())  {
            int counter = 1, size, limit = 0;
            double chance, additionalChance = 0;
            Collections.shuffle(map.getValue());
            size = map.getValue().size();
            for (Block block : map.getValue()) {
                chance = OreGenChange.getInstance().getCustomConfig().getChance(block.getWorld(), block.getType());
                if (limit == 0) {
                    limit = (int) (size * chance);
                    if (limit > size) {
                        additionalChance = chance - 1;
                    }
                }
                if (counter > limit) {
                    OreGenChange.debug("Limit is " + limit + " Counter is " + counter);
                    OreGenChange.debug("Setting " + block.getType().name() + " to " +
                            OreGenChange.getInstance().getCustomConfig().getDefaultMaterial());
                    block.setType(OreGenChange.getInstance().getCustomConfig().getDefaultMaterial());
                } else if (additionalChance < 1) {
                    Random generator = new Random();
                    if (generator.nextDouble() > additionalChance) {
                        if (!block.getRelative(BlockFace.NORTH).getType().equals(map.getKey())) {
                            OreGenChange.debug("Set block " + block.getRelative(BlockFace.NORTH).getType().name() +
                                    " to " + map.getKey() + " at north");
                            block.setType(map.getKey());
                        } else if (!block.getRelative(BlockFace.SOUTH).getType().equals(map.getKey())) {
                            OreGenChange.debug("Set block " + block.getRelative(BlockFace.SOUTH).getType().name() +
                                    " to " + map.getKey() + " at south");
                            block.setType(map.getKey());
                        } else if (!block.getRelative(BlockFace.EAST).getType().equals(map.getKey())) {
                            OreGenChange.debug("Set block " + block.getRelative(BlockFace.EAST).getType().name() +
                                    " to " + map.getKey() + " at east");
                            block.setType(map.getKey());
                        } else if (!block.getRelative(BlockFace.WEST).getType().equals(map.getKey())) {
                            OreGenChange.debug("Set block " + block.getRelative(BlockFace.WEST).getType().name() +
                                    " to " + map.getKey() + " at west");
                            block.setType(map.getKey());
                        }
                    }
                } else if (additionalChance >= 1) {
                    for (int i = 0; additionalChance > i; i++) {
                        if (!block.getRelative(BlockFace.NORTH).getType().equals(map.getKey())) {
                            OreGenChange.debug("Set block " + block.getRelative(BlockFace.NORTH).getType().name() +
                                    " to " + map.getKey() + " at north");
                            block.setType(map.getKey());
                        } else if (!block.getRelative(BlockFace.SOUTH).getType().equals(map.getKey())) {
                            OreGenChange.debug("Set block " + block.getRelative(BlockFace.SOUTH).getType().name() +
                                    " to " + map.getKey() + " at south");
                            block.setType(map.getKey());
                        } else if (!block.getRelative(BlockFace.EAST).getType().equals(map.getKey())) {
                            OreGenChange.debug("Set block " + block.getRelative(BlockFace.EAST).getType().name() +
                                    " to " + map.getKey() + " at east");
                            block.setType(map.getKey());
                        } else if (!block.getRelative(BlockFace.WEST).getType().equals(map.getKey())) {
                            OreGenChange.debug("Set block " + block.getRelative(BlockFace.WEST).getType().name() +
                                    " to " + map.getKey() + " at west");
                            block.setType(map.getKey());
                        }
                    }
                } else {
                    OreGenChange.debug("DO NOT CHANGE " + block.getType().name());
                }
                counter++;
            }
        }
        temporary.clear();
        this.blocks.clear();
    }
}
