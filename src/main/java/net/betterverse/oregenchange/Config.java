package net.betterverse.oregenchange;

import org.bukkit.Material;
import org.bukkit.World;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Config {
    private Map<String, Map<Material, Double>> worlds = new HashMap<String, Map<Material, Double>> ();
    private Material defaultMaterial = Material.STONE;
    private boolean debug = false;
    public enum ReplaceableMaterials {
        AIR(0), STONE(1), GRASS(2), DIRT(3), COBBLESTONE(4), SAND(12), GRAVEL(13), GOLD_ORE(14), IRON_ORE(15),
        COAL_ORE(16), LAPIS_ORE(21), SANDSTONE(24), DIAMOND_ORE(56), SOIL(60), REDSTONE_ORE(73),
        GLOWING_REDSTONE_ORE(74);

        private int id;
        ReplaceableMaterials (int id) {
            this.id = id;
        }
        
        public int getId() {
            return this.id;
        }
    }

    public Config(OreGenChange instance) {
        if (instance.getConfig().contains("worlds")) {
            this.defaultMaterial = Material.getMaterial(instance.getConfig().getInt("worlds.default", 1));
            this.debug = instance.getConfig().getBoolean("worlds.debug", false);
            Map<String, Object> nodeMap = instance.getConfig().getConfigurationSection("worlds").getValues(false);
            for (String world : nodeMap.keySet()) {
                OreGenChange.debug("Found node " + world + "...");
                if (instance.getConfig().getBoolean("worlds." + world + ".enabled")) {
                    Map<Material, Double> materials = new HashMap<Material, Double>();
                    Map<String, Object> valueMap =
                            instance.getConfig().getConfigurationSection("worlds." + world + ".blocks").getValues(true);
                    for (String block : valueMap.keySet()) {
                        OreGenChange.debug("Found subnode " + block + ": " +
                                                instance.getConfig().getDouble("worlds." + world + ".blocks." + block));
                        if ((instance.getConfig().isDouble("worlds." + world + ".blocks." + block) ||
                                instance.getConfig().isInt("worlds." + world + ".blocks." + block)) &&
                                isReplaceableMaterial(Integer.parseInt(block))) {
                            OreGenChange.debug("Found replaceable subnode: " + block);
                            materials.put(Material.getMaterial(Integer.parseInt(block)),
                                            instance.getConfig().getDouble("worlds." + world + ".blocks." + block));
                        }
                    }
                    this.worlds.put(world, materials);
                }
            }
        }
        printMap(this.worlds);
        instance.saveConfig();
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private static void printMap(Map map) {
        Iterator<String> iterator = map.keySet().iterator();

        while (iterator.hasNext()) {
            String key = iterator.next();
            String value = map.get(key).toString();
            OreGenChange.debug(key + " " + value);
        }
    }
    
    public Material getDefaultMaterial() {
        return this.defaultMaterial;
    }

    public boolean isDebug() {
        return this.debug;
    }

    public boolean isReplaceableMaterial(Material material) {
        for (ReplaceableMaterials c : ReplaceableMaterials.values()) {
            if (c.name().equalsIgnoreCase(material.name())) {
                return true;
            }
        }
        return false;
    }

    public boolean isReplaceableMaterial(int materialId) {
        for (ReplaceableMaterials c : ReplaceableMaterials.values()) {
            OreGenChange.debug(c + " - " + materialId);
            if (c.getId() == materialId) {
                return true;
            }
        }
        return false;
    }
    
    public ReplaceableMaterials[] getReplaceableMaterials() {
        return ReplaceableMaterials.values();
    }

    public boolean isWatchedMaterial(World world, int materialId) {
        return isWatchedMaterial(world.getName(), materialId);
    }

    public boolean isWatchedMaterial(String world, int materialId) {
        return isWatchedMaterial(world, Material.getMaterial(materialId));
    }
    
    public boolean isWatchedMaterial(World world, Material material) {
        return isWatchedMaterial(world.getName(), material);
    }

    public boolean isWatchedMaterial(String world, Material material) {
        return this.isWatchedWorld(world) && this.worlds.get(world).containsKey(material);
    }
    
    public boolean isWatchedWorld(World world) {
        return isWatchedWorld(world.getName());
    }

    public boolean isWatchedWorld(String world) {
        return this.worlds.containsKey(world);
    }
    
    public double getChance(World world, Material material) {
        return getChance(world.getName(), material);
    }

    public double getChance(World world, int materialId) {
        return getChance(world.getName(), materialId);
    }

    public double getChance(String world, int materialId) {
        return getChance(world, Material.getMaterial(materialId));
    }

    public double getChance(String world, Material material) {
        if (isWatchedMaterial(world, material)) {
            return this.worlds.get(world).get(material);
        }
        return 1;
    }

    public Map<Material, Double> getMaterialsWatched(World world) {
        return getMaterialsWatched(world.getName());
    }

    public Map<Material, Double> getMaterialsWatched(String world) {
        if (isWatchedWorld(world)) {
            return this.worlds.get(world);
        }
        return new HashMap<Material, Double>();
    }
}