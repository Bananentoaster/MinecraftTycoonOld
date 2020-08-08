package me.bananentoast.minecrafttycoon.command;

import me.bananentoast.minecrafttycoon.MinecraftTycoon;
import me.bananentoast.minecrafttycoon.entity.TycoonPlayer;
import me.bananentoast.minecrafttycoon.entity.npc.Npc;
import me.bananentoast.minecrafttycoon.entity.npc.enums.NpcAnimationType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public class PlayerCommand implements CommandExecutor {

    private MinecraftTycoon instance;

    public PlayerCommand(MinecraftTycoon instance) {
        this.instance = instance;
        instance.getCommand("player").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;
            if (args.length > 0) {
                if (args[0].toLowerCase().equals("list")) {
                    String players = "Players:";
                    List<TycoonPlayer> list = instance.getPlayerManager().getPlayers();
                    for (TycoonPlayer tycoonPlayer : list) {
                        players = players + " " + tycoonPlayer.toString();
                    }
                    player.sendMessage(players);
                    return true;
                } else if (args[0].toLowerCase().equals("crash")) {
                    for (int i = 0; i < 10000; i++) {
                        UUID uuid = instance.getNpcManager().createNpc();
                        Npc npc = instance.getNpcManager().getNpc(uuid);
                        if (npc != null) {
                            npc.join(player);
                            npc.animation(player, NpcAnimationType.ARM_SWING);

                        }
                    }
                    return true;
                }
            }
        }
        return false;
    }

}
