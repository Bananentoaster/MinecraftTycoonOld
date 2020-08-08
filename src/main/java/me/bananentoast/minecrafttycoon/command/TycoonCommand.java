package me.bananentoast.minecrafttycoon.command;

import me.bananentoast.minecrafttycoon.MinecraftTycoon;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TycoonCommand implements CommandExecutor {

    private MinecraftTycoon instance;

    public TycoonCommand(MinecraftTycoon instance) {
        this.instance = instance;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String name, String[] args) {
        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;
            if (!player.isOp())
                return false;

        }
        return false;
    }
}
