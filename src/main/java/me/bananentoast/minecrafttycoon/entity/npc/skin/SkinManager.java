package me.bananentoast.minecrafttycoon.entity.npc.skin;

import me.bananentoast.minecrafttycoon.MinecraftTycoon;
import me.bananentoast.minecrafttycoon.entity.npc.NpcManager;
import me.bananentoast.minecrafttycoon.entity.npc.skin.download.SkinDownloader;

import java.util.HashMap;
import java.util.UUID;

public class SkinManager {

    private MinecraftTycoon instance;
    private NpcManager npcManager;

    private SkinDownloader skinDownloader;
    private SkinStorageManager skinStorageManager;

    private HashMap<UUID, Skin> skinMap = new HashMap<>();
    private HashMap<String, UUID> aliasMap = new HashMap<>();

    public SkinManager(MinecraftTycoon instance, NpcManager npcManager) {
        this.instance = instance;
        this.npcManager = npcManager;
        skinDownloader = new SkinDownloader(instance, this);
        skinStorageManager = new SkinStorageManager(instance, this);
    }

    public UUID getNewSkinId() {
        UUID randomId = UUID.randomUUID();
        if (skinMap.containsKey(randomId))
            return getNewSkinId();
        return randomId;
    }

    public UUID downloadSkin(String playerName) {
        Skin skin = skinDownloader.downloadFromName(playerName);
        if (skin != null) {
            skin.setAlias("#" + skin.getId().toString().substring(0, 5));
            skinMap.put(skin.getId(), skin);
            return skin.getId();
        }
        return null;
    }

    public Skin getSkin(UUID skinId) {
        if (skinMap.containsKey(skinId))
            return skinMap.get(skinId);
        instance.error("Invalid skinId! (" + skinId + ")");
        return null;
    }

    public UUID getSkinId(String skinAlias) {
        if (aliasMap.containsKey(skinAlias))
            return aliasMap.get(skinAlias);
        instance.error("Invalid skinAlias! (" + skinAlias + ")");
        return null;
    }

    public boolean setSkinAlias(Skin skin, String newAlias) {
        if (!aliasMap.containsKey(newAlias)) {
            aliasMap.remove(skin.getAlias());
            aliasMap.put(newAlias, skin.getId());
            return true;
        }
        System.out.println("SkinAlias already in use! (" + newAlias + ")");
        return false;
    }

    public void saveSkins() {
        skinStorageManager.saveSkins(skinMap);
    }

    public void loadSkins() {
        skinStorageManager.loadSkins(skinMap, aliasMap);
    }

}
