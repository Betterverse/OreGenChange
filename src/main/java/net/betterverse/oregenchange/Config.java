package net.betterverse.oregenchange;

import org.bukkit.World;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Config {

    World world;
    File configDir;
    OreGenChange plugin;
    public ArrayList<String> worlds = new ArrayList<String>();
    HashMap<String, Double> settings = new HashMap<String, Double>();
    private String defaultConfig = ("# OreGenChange config file\r\n" +
            "# This file is rather fragile, try not to do anything other than alter the numbers and @ line!\r\n" +
            "# To enable this world, set to true\r\n" +
            "@enabled? false\r\n" +
            "# Chance is the chance of a vein generating per pass. 0.1 - 1\r\n" +
            "# Passes are the number of chances of generating per chunk, should be used in conjunction with Chance\r\n" +
            "# SizeMax is the maximum iterations the vein can make\r\n" +
            "# SizeMin is the minimum iterations the vein can make\r\n" +
            "# MaxHeight is the maximum height the ore can generate at\r\n" +
            "ironChance 0.7\r\n" +
            "ironPasses 6\r\n" +
            "ironSizeMax 8\r\n" +
            "ironSizeMin 4\r\n" +
            "ironMaxHeight 80\r\n" +
            "diamondChance 0.2\r\n" +
            "diamondPasses 3\r\n" +
            "diamondSizeMax 3\r\n" +
            "diamondSizeMin 1\r\n" +
            "diamondMaxHeight 18\r\n" +
            "coalChance 0.8\r\n" +
            "coalPasses 6\r\n" +
            "coalSizeMax 12\r\n" +
            "coalSizeMin 2\r\n" +
            "coalMaxHeight 95\r\n" +
            "lapisChance 0.4\r\n" +
            "lapisPasses 3\r\n" +
            "lapisSizeMax 5\r\n" +
            "lapisSizeMin 2\r\n" +
            "lapisMaxHeight 30\r\n" +
            "redstoneChance 0.5\r\n" +
            "redstonePasses 4\r\n" +
            "redstoneSizeMax 8\r\n" +
            "redstoneSizeMin 4\r\n" +
            "redstoneMaxHeight 32\r\n" +
            "goldChance 0.4\r\n" +
            "goldPasses 2\r\n" +
            "goldSizeMax 8\r\n" +
            "goldSizeMin 2\r\n" +
            "goldMaxHeight 28\r\n");

    public Config(OreGenChange plugin, World world) {
        this.plugin = plugin;
        this.world = world;
    }

    public HashMap<String, Double> init() {
        System.out.println("durrrr");
        this.configDir = plugin.getDataFolder();
        if (!this.configDir.exists())
            this.configDir.mkdir();

        File configFile = new File(configDir.getAbsoluteFile() + "/" + world.getName() +".txt");
        if (!configFile.exists())
            try {
                createConfig(configFile);
            } catch (IOException e) {
                e.printStackTrace();
            }

        FileReader r = null;
        try {
            r = new FileReader(configFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        BufferedReader in = new BufferedReader(r);
        String l;
        try {
            while ((l = in.readLine()) != null) {
                if (!l.startsWith("#")) {
                    if (l.startsWith("@enabled?")) {
                        if (l.split(" ")[1].equals("true"))
                            plugin.worldEnabled.add(world);
                    } else {
                        String[] line = l.split(" ");
                        String key = line[0];
                        double value = Double.parseDouble(line[1]);
                        settings.put(key, value);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return settings;
    }

    private void createConfig(File configFile) throws IOException {
        configFile.createNewFile();
        FileWriter w = new FileWriter(configFile);
        BufferedWriter out = new BufferedWriter(w);
        out.write(defaultConfig);
        out.close();
    }
}
