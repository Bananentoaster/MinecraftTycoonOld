package me.bananentoast.minecrafttycoon.entity;

import me.bananentoast.minecrafttycoon.MinecraftTycoon;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PlayerManager {

    private MinecraftTycoon instance;

    private HashMap<Player, TycoonPlayer> playerMap = new HashMap<>();

    public PlayerManager(MinecraftTycoon instance) {
        this.instance = instance;
    }

    public TycoonPlayer addPlayer(Player player) {
        if (!playerMap.containsKey(player)) {
            TycoonPlayer newTycoonPlayer = new TycoonPlayer(instance, player);
            playerMap.put(player, newTycoonPlayer);
            return newTycoonPlayer;
        }
        return null;
    }

    public void removePlayer(Player player) {
        if (playerMap.containsKey(player)) {
            playerMap.remove(player);
        }
    }

    public TycoonPlayer getPlayer(Player player) {
        if (playerMap.containsKey(player))
            return playerMap.get(player);
        return null;
    }

    private List<TycoonPlayer> getPlayersList = new ArrayList<>();
    public List<TycoonPlayer> getPlayers() {
        getPlayersList.clear();
        getPlayersList.addAll(playerMap.values());
        return getPlayersList;
    }

}
