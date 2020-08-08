package me.bananentoast.minecrafttycoon.listener;

import com.google.gson.JsonElement;
import io.netty.channel.*;
import me.bananentoast.minecrafttycoon.MinecraftTycoon;
import me.bananentoast.minecrafttycoon.util.Reflection;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.lang.reflect.Field;

public class PlayerJoinListener extends Reflection implements Listener {

    private MinecraftTycoon instance;

    public PlayerJoinListener(MinecraftTycoon instance) {
        this.instance = instance;
        instance.getServer().getPluginManager().registerEvents(this, instance);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {

        instance.getPlayerManager().addPlayer(event.getPlayer());

        injectPlayer(event.getPlayer());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {

        instance.getPlayerManager().removePlayer(event.getPlayer());

        removePlayer(event.getPlayer());
    }

    public void removePlayer(final Player player) {
        Channel channel = ((CraftPlayer)player).getHandle().playerConnection.networkManager.channel;
        channel.eventLoop().submit(() -> {
            channel.pipeline().remove(player.getName());
            return null;
        });
    }

    private void injectPlayer(Player player) {
        ChannelDuplexHandler channelDuplexHandler = new ChannelDuplexHandler() {
            @Override
            public void channelRead(ChannelHandlerContext channelHandlerContext, Object object) throws Exception {
                super.channelRead(channelHandlerContext, object);
            }

            @Override
            public void write(ChannelHandlerContext channelHandlerContext, Object object, ChannelPromise channelPromise) throws Exception {

                if (object instanceof PacketPlayOutTabComplete) {
                    PacketPlayOutTabComplete packetPlayOutTabComplete = (PacketPlayOutTabComplete) object;
                    Field field = packetPlayOutTabComplete.getClass().getDeclaredField("a");
                    field.setAccessible(true);
                    String[] args = (String[]) field.get(packetPlayOutTabComplete);
                    String[] newargs = new String[1];
                    newargs[0] = "nope";
                    field.set(packetPlayOutTabComplete, newargs);
                    //Bukkit.broadcastMessage("Args: " + args[0]);
                }


                if (object instanceof PacketPlayOutBlockChange) {
                    PacketPlayOutBlockChange packet = (PacketPlayOutBlockChange) object;
                    BaseBlockPosition blockPosition = (BaseBlockPosition) getValue(packet, "a");
                    //Bukkit.broadcastMessage("§b§lBlock Change at:  §fx: " + blockPosition.getX() + " | y: " + blockPosition.getY() + " | z:" + blockPosition.getZ());
                }

                if (object instanceof PacketPlayOutBlockBreakAnimation) {
                    PacketPlayOutBlockBreakAnimation packet = (PacketPlayOutBlockBreakAnimation) object;
                    //Bukkit.broadcastMessage("§e§lBreak-Status: §f" + getValue(packet, "c").toString() + " §b§lPos: " + getValue(packet, "b"));
                }

                if (object instanceof PacketPlayOutChat) {
                    PacketPlayOutChat packet = (PacketPlayOutChat) object;
                    //setValue(packet, "a", IChatBaseComponent.ChatSerializer.a("{\"text\":\"" + 4 + "\"}"));
                    //setValue(packet, "b", (byte) 0);
                    IChatBaseComponent chatBaseComponent = (IChatBaseComponent) getValue(packet, "a");
                    //System.out.println("Text: " + chatBaseComponent.toString());
                }

                super.write(channelHandlerContext, object, channelPromise);
            }
        };

        ChannelPipeline pipeline = ((CraftPlayer)player).getHandle().playerConnection.networkManager.channel.pipeline();
        pipeline.addBefore("packet_handler", player.getName(), channelDuplexHandler);
    }

}
