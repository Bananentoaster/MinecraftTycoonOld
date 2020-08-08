package me.bananentoast.minecrafttycoon.command;

import me.bananentoast.minecrafttycoon.MinecraftTycoon;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class PacketCommand implements CommandExecutor {

    private MinecraftTycoon instance;

    public PacketCommand(MinecraftTycoon instance) {
        this.instance = instance;
        instance.getCommand("packet").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        return false;
    }

}
