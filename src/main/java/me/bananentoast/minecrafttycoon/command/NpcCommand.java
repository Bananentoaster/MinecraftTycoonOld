package me.bananentoast.minecrafttycoon.command;

import me.bananentoast.minecrafttycoon.MinecraftTycoon;
import me.bananentoast.minecrafttycoon.entity.npc.Npc;
import me.bananentoast.minecrafttycoon.entity.npc.skin.Skin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class NpcCommand implements CommandExecutor {

    private MinecraftTycoon instance;

    public NpcCommand(MinecraftTycoon instance) {
        this.instance = instance;
        instance.getCommand("npc").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String name, String[] args) {
        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;
            if (!player.isOp())
                return false;
            if (args.length > 0) {
                if (args[0].toLowerCase().equals("create")) {
                    UUID newNpcId = instance.getNpcManager().createNpc();
                    Npc newNpc = instance.getNpcManager().getNpc(newNpcId);
                    if (newNpc != null) {
                        player.sendMessage("Created Npc with id: " + newNpcId + " and alias: " + newNpc.getAlias());
                    }
                    return true;
                } else if (args[0].toLowerCase().equals("save")) {
                    instance.getNpcManager().saveNpcs();
                    player.sendMessage("Saved Npcs");
                    return true;
                } else if (args[0].toLowerCase().equals("load")) {
                    instance.getNpcManager().loadNpcs();
                    player.sendMessage("Loaded Npcs");
                    return true;
                } else if (args[0].toLowerCase().equals("skin")) {
                    if (args.length > 1) {
                        if (args[1].toLowerCase().equals("download")) {
                            if (args.length > 2) {
                                UUID newSkinId = instance.getNpcManager().getSkinManager().downloadSkin(args[2]);
                                Skin newSkin = instance.getNpcManager().getSkinManager().getSkin(newSkinId);
                                if (newSkin != null) {
                                    player.sendMessage("Created Skin with id: " + newSkinId + " and alias: " + newSkin.getAlias());
                                }
                            }
                            return true;
                        } else if (args[1].toLowerCase().equals("save")) {
                            instance.getNpcManager().getSkinManager().saveSkins();
                            player.sendMessage("Saved Skins");
                            return true;
                        } else if (args[1].toLowerCase().equals("load")) {
                            instance.getNpcManager().getSkinManager().loadSkins();
                            player.sendMessage("Loaded Skins");
                            return true;
                        }
                        String skinAlias = args[1];
                        Skin skin = instance.getNpcManager().getSkinManager().getSkin(instance.getNpcManager().getSkinManager().getSkinId(skinAlias));
                        if (skin == null) {
                            player.sendMessage("Invalid skinAlias! (" + args[1] + ")");
                            return false;
                        }
                        if (args.length > 2) {
                            if (args[2].toLowerCase().equals("alias")) {
                                if (args.length > 3) {
                                    boolean successful = skin.setAlias(args[3]);
                                    if (successful)
                                        player.sendMessage("Changed alias from skin(" + skin.getId() + ") to " + args[3]);
                                }
                            }
                        }
                    }
                    return true;
                }
                String npcAlias = args[0];
                Npc npc = instance.getNpcManager().getNpc(instance.getNpcManager().getNpcId(npcAlias));
                if (npc == null) {
                    player.sendMessage("Invalid npcAlias! (" + args[0] + ")");
                    return false;
                }
                if (args.length > 1) {
                    if (args[1].toLowerCase().equals("join")) {
                        npc.join();
                    } else if (args[1].toLowerCase().equals("alias")) {
                        if (args.length > 2) {
                            boolean successful = npc.setAlias(args[2]);
                            if (successful)
                                player.sendMessage("Changed alias from npc(" + npc.getNpcId() + ") to " + args[2]);
                        }
                    } else if (args[1].toLowerCase().equals("skin")) {
                        if (args.length > 2) {
                            String skinAlias = args[2];
                            Skin skin = instance.getNpcManager().getSkinManager().getSkin(instance.getNpcManager().getSkinManager().getSkinId(skinAlias));
                            if (skin != null) {
                                npc.changeSkin(skin);
                                player.sendMessage("Changed to skin(" + skinAlias + ") of npc(" + npcAlias + ")");
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

}
