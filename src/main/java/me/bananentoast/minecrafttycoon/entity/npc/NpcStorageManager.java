package me.bananentoast.minecrafttycoon.entity.npc;

import me.bananentoast.minecrafttycoon.MinecraftTycoon;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.HashMap;
import java.util.UUID;

public class NpcStorageManager {

    private MinecraftTycoon instance;
    private NpcManager npcManager;

    private File file;

    public NpcStorageManager(MinecraftTycoon instance, NpcManager npcManager) {
        this.instance = instance;
        this.npcManager = npcManager;
        file = new File("plugins//MinecraftTycoon//Npc//NpcStorage.json");
    }

    public synchronized void loadNpcs(HashMap<UUID, Npc> npcMap, HashMap<String, UUID> aliasMap) {
        try {
            Object parse = new JSONParser().parse(new FileReader(file.getAbsolutePath()));
            JSONArray array = (JSONArray) parse;
            for (Object object : array) {
                JSONObject jsonObject = (JSONObject) object;
                UUID id = UUID.fromString(jsonObject.get("id").toString());
                Npc npc = new Npc(instance, id, jsonObject.get("name").toString(), instance.getJsonUtil().stringToLocation(jsonObject.get("location").toString()));
                npc.changeSkin(UUID.fromString(jsonObject.get("skinId").toString()));
                npcMap.put(id, npc);
                npc.setAlias(jsonObject.get("alias").toString());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveNpcs(HashMap<UUID, Npc> npcMap) {
        JSONArray jsonArray = new JSONArray();
        for (UUID id : npcMap.keySet()) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", npcMap.get(id).getNpcId().toString());
            jsonObject.put("alias", npcMap.get(id).getAlias());
            jsonObject.put("name", npcMap.get(id).getName());
            jsonObject.put("location", instance.getJsonUtil().locationToString(npcMap.get(id).getLocation()));
            jsonObject.put("skinId", npcMap.get(id).getSkin().getId().toString());
            jsonArray.add(jsonObject);
        }
        try {
            FileWriter writer = new FileWriter(file);
            writer.write(jsonArray.toJSONString());
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
