package me.bananentoast.minecrafttycoon.region;

import me.bananentoast.minecrafttycoon.MinecraftTycoon;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.material.Directional;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Region {

    private MinecraftTycoon instance;

    private Location loc1, loc2;

    public Region(MinecraftTycoon instance) {
        this.instance = instance;
    }

    public Region(MinecraftTycoon instance, Location loc1, Location loc2) {
        this.instance = instance;
        this.loc1 = loc1;
        this.loc2 = loc2;
    }

    public List<Block> toList() {
        List<Block> blocks = new ArrayList<>();

        if (loc1.getBlockX() < loc2.getBlockX()) {
            for (int blockx = loc1.getBlockX(); blockx <= loc2.getBlockX(); blockx++) {
                for (int blockz = loc1.getBlockZ(); blockz <= loc2.getBlockZ(); blockz++) {
                    blocks.add(Bukkit.getWorld("world").getBlockAt(blockx, loc1.getBlockY(), blockz));
                }
            }
        }

        return blocks;
    }

    public JSONObject saveJson() {
        JSONObject json = new JSONObject();
        JSONArray array = new JSONArray();
        Block lastBlock = null;
        int sameBlock = 0;
        for (Block block : toList()) {
            if (lastBlock == null || (lastBlock.getLocation() == block.getLocation() && lastBlock.getData() == block.getData())) {
                sameBlock++;
            } else {
                JSONObject jsonBlock = new JSONObject();
                jsonBlock.put("type", block.getType().name());
                jsonBlock.put("data", block.getData());
                jsonBlock.put("amount", sameBlock);
            }
        }
        json.put("array", array);
        return json;
    }

    public void loadJson() {

    }

}
