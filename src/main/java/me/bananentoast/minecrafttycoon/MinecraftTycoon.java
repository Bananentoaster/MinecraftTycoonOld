package me.bananentoast.minecrafttycoon;

import me.bananentoast.minecrafttycoon.command.NpcCommand;
import me.bananentoast.minecrafttycoon.command.PacketCommand;
import me.bananentoast.minecrafttycoon.command.PlayerCommand;
import me.bananentoast.minecrafttycoon.command.TycoonCommand;
import me.bananentoast.minecrafttycoon.entity.PlayerManager;
import me.bananentoast.minecrafttycoon.entity.npc.NpcManager;
import me.bananentoast.minecrafttycoon.listener.PlayerJoinListener;
import me.bananentoast.minecrafttycoon.region.Region;
import me.bananentoast.minecrafttycoon.util.JsonUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;

public class MinecraftTycoon extends JavaPlugin {

    private PlayerManager playerManager;
    private NpcManager npcManager;

    private JsonUtil jsonUtil;

    private PlayerJoinListener playerJoinListener;

    private boolean debug = true;

    @Override
    public void onEnable() {

        playerManager = new PlayerManager(this);
        npcManager = new NpcManager(this);

        jsonUtil = new JsonUtil(this);

        new NpcCommand(this);
        new PacketCommand(this);
        new PlayerCommand(this);
        new TycoonCommand(this);

        new PlayerJoinListener(this);

        Region testRegion = new Region(this, new Location(Bukkit.getWorld("world"), -5, 108, -5), new Location(Bukkit.getWorld("world"), 5, 108, 5));

        testRegion.toList().forEach(block -> {
            Bukkit.broadcastMessage(block.getType().toString());
            block.setType(Material.DIAMOND_BLOCK);
        });

        /*
        LiteStorage liteStorage = new LiteStorage("C://Jonas/Plug/Server/plugins/", "test.db");

        liteStorage.connect();

        liteStorage.update("create table if not exists Skins (SkinId int not null primary key, Value varchar, Signature varchar)");

        liteStorage.update("insert into Skins (`SkinId`, `Value`, `Signature`) values ('" + 1 + "', ' Value123 ', ' Signature123243 ')");

        liteStorage.disconnect();b

         */

    }

    public JsonUtil getJsonUtil() {
        return jsonUtil;
    }

    public PlayerManager getPlayerManager() {
        return playerManager;
    }

    public NpcManager getNpcManager() {
        return npcManager;
    }

    public PlayerJoinListener getPlayerJoinListener() {
        return playerJoinListener;
    }

    public void error(String errorMessage) {
        if (debug)
            System.out.println(errorMessage);
    }

}
