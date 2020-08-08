package me.bananentoast.minecrafttycoon.entity.npc;

import me.bananentoast.minecrafttycoon.MinecraftTycoon;
import me.bananentoast.minecrafttycoon.entity.npc.skin.SkinManager;
import org.bukkit.Bukkit;

import java.util.HashMap;
import java.util.UUID;

public class NpcManager {

    private MinecraftTycoon instance;
    private SkinManager skinManager;
    private NpcStorageManager npcStorageManager;

    private HashMap<UUID, Npc> npcMap = new HashMap<>();
    private HashMap<String, UUID> aliasMap = new HashMap<>();

    public NpcManager(MinecraftTycoon instance) {
        this.instance = instance;
        skinManager = new SkinManager(instance, this);
        npcStorageManager = new NpcStorageManager(instance, this);
    }

    public UUID getNewNpcId() {
        UUID randomId = UUID.randomUUID();
        if (npcMap.containsKey(randomId))
            return getNewNpcId();
        return randomId;
    }

    public UUID createNpc() {
        Npc newNpc = new Npc(instance, getNewNpcId(), "npc", Bukkit.getWorlds().get(0).getSpawnLocation());
        newNpc.setAlias("#" + newNpc.getNpcId().toString().substring(0, 5));
        aliasMap.put(newNpc.getAlias(), newNpc.getNpcId());
        npcMap.put(newNpc.getNpcId(), newNpc);
        return newNpc.getNpcId();
    }

    public Npc getNpc(UUID npcId) {
        if (npcMap.containsKey(npcId))
            return npcMap.get(npcId);
        instance.error("Invalid npcId! (" + npcId + ")");
        return null;
    }

    public UUID getNpcId(String npcAlias) {
        if (aliasMap.containsKey(npcAlias))
            return aliasMap.get(npcAlias);
        instance.error("Invalid npcAlias! (" + npcAlias + ")");
        return null;
    }

    public Boolean setNpcAlias(Npc npc, String newAlias) {
        if (!aliasMap.containsKey(newAlias)) {
            aliasMap.remove(npc.getAlias());
            aliasMap.put(newAlias, npc.getNpcId());
            return true;
        }
        System.out.println("NpcAlias already in use! (" + newAlias + ")");
        return false;
    }

    public void saveNpcs() {
        npcStorageManager.saveNpcs(npcMap);
    }

    public void loadNpcs() {
        npcStorageManager.loadNpcs(npcMap, aliasMap);
    }

    public SkinManager getSkinManager() {
        return skinManager;
    }

}
