package me.bananentoast.minecrafttycoon.entity.npc.skin;

import me.bananentoast.minecrafttycoon.MinecraftTycoon;
import me.bananentoast.minecrafttycoon.util.LiteStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.UUID;

public class SkinStorageManager extends LiteStorage {

    private MinecraftTycoon instance;
    private SkinManager skinManager;

    public SkinStorageManager(MinecraftTycoon instance, SkinManager skinManager) {

        super("plugins//MinecraftTycoon//", "SkinStorage.db");

        connect();

        update("create table if not exists Skins (SkinId varchar, Value varchar, Signature varchar, Alias varchar)");

        this.instance = instance;
        this.skinManager = skinManager;
    }

    public synchronized void loadSkins(HashMap<UUID, Skin> skinMap, HashMap<String, UUID> aliasMap) {
        connect();

        ResultSet result = query("select * from Skins");

        try {
            skinMap.clear();
            aliasMap.clear();
            while (result.next()) {
                UUID id = UUID.fromString(result.getString("SkinId"));
                Skin skin = new Skin(instance, id, result.getString("Value"), result.getString("Signature"));
                skinMap.put(id, skin);
                skin.setAlias(result.getString("Alias"));
                aliasMap.put(skin.getAlias(), skin.getId());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void saveSkins(HashMap<UUID, Skin> skinMap) {

        update("delete from Skins");

        skinMap.forEach((uuid, skin) -> {
            update("insert into Skins (`SkinId`, `Value`, `Signature`, `Alias`) values ('" + uuid.toString() +  "', '" + skin.getValue() + "', '" + skin.getSignature() + "', '" + skin.getAlias() + "')");
        });

        disconnect();
    }

}
