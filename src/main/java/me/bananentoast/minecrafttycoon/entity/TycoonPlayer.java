package me.bananentoast.minecrafttycoon.entity;

import me.bananentoast.minecrafttycoon.MinecraftTycoon;
import org.bukkit.entity.Player;

public class TycoonPlayer extends BaseTycoonEntity{

    private MinecraftTycoon instance;

    private Player player;

    public TycoonPlayer(MinecraftTycoon instance, Player player) {
        this.instance = instance;
        this.player = player;
    }

}
