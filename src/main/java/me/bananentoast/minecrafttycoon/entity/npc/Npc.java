package me.bananentoast.minecrafttycoon.entity.npc;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import me.bananentoast.minecrafttycoon.MinecraftTycoon;
import me.bananentoast.minecrafttycoon.entity.npc.enums.NpcAnimationType;
import me.bananentoast.minecrafttycoon.entity.npc.enums.NpcSlotType;
import me.bananentoast.minecrafttycoon.entity.npc.enums.NpcStatusType;
import me.bananentoast.minecrafttycoon.entity.npc.skin.Skin;
import me.bananentoast.minecrafttycoon.util.Reflection;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.util.CraftChatMessage;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public class Npc extends Reflection {

    private MinecraftTycoon instance;

    private int entityId;
    private GameProfile gameProfile;

    private UUID npcId;
    private String alias;
    private Skin skin;

    private Location bedLocation;

    private Location location;
    private String name;

    public Npc(MinecraftTycoon instance, UUID npcId, String name, Location location) {
        this.instance = instance;
        this.npcId = npcId;
        this.name = name;

        createPackets();

        bedLocation = location.clone();
        bedLocation.zero().setY(1);

        entityId = (int) Math.ceil(Math.random() * 1000) + 5000;
        gameProfile = new GameProfile(UUID.randomUUID(), this.name);
        teleport(location.clone());
    }

    private void createPackets() {
        packetByPassJoin = new PacketPlayOutNamedEntitySpawn();
        packetLeave = new PacketPlayOutEntityDestroy();
        packetAddTabList = new PacketPlayOutPlayerInfo();
        packetAddTabListData = packetAddTabList.new PlayerInfoData(gameProfile, 1, WorldSettings.EnumGamemode.NOT_SET, CraftChatMessage.fromString("npc")[0]);
        packetRemoveTabList = new PacketPlayOutPlayerInfo();
        packetRemoveTabListData = packetAddTabList.new PlayerInfoData(gameProfile, 1, WorldSettings.EnumGamemode.NOT_SET, CraftChatMessage.fromString("npc")[0]);
        packetTeleport = new PacketPlayOutEntityTeleport();
        packetHeadLook = new PacketPlayOutEntity.PacketPlayOutEntityLook();
        packetHeadRotation = new PacketPlayOutEntityHeadRotation();
        packetAnimation = new PacketPlayOutAnimation();
        packetStatus = new PacketPlayOutEntityStatus();
        packetEquip = new PacketPlayOutEntityEquipment();
        packetSleep = new PacketPlayOutBed();
    }

    private PacketPlayOutNamedEntitySpawn packetByPassJoin;
    private void join(Player player, boolean toAll) {
        PacketPlayOutNamedEntitySpawn packet = packetByPassJoin;

        setValue(packet, "a", entityId);
        setValue(packet, "b", gameProfile.getId());
        setValue(packet, "c", getFixLocation(location.getX()));
        setValue(packet, "d", getFixLocation(location.getY()));
        setValue(packet, "e", getFixLocation(location.getZ()));
        setValue(packet, "f", getFixRotation(location.getYaw()));
        setValue(packet, "g", getFixRotation(location.getPitch()));
        setValue(packet, "h", 0);

        DataWatcher dataWatcher = new DataWatcher(null);
        dataWatcher.a(6, (float) 20); //set Health to 20
        dataWatcher.a(10, (byte) 127); //Set Skin
        setValue(packet, "i", dataWatcher);

        if (toAll) {
            addToTabList();
            sendPacket(packet);
            teleport(location);
        } else {
            addToTabList(player);
            sendPacket(player, packet);
            teleport(player, location);
        }
    }

    public void join(Player player) {
        join(player, false);
    }

    public void join() {
        join(null, true);
    }

    private PacketPlayOutEntityDestroy packetLeave;
    private void leave(Player player, boolean toAll) {
        PacketPlayOutEntityDestroy packet = packetLeave;

        setValue(packet, "a", new int[] {entityId});

        if (toAll) {
            removeFromTabList();
            sendPacket(packet);
        } else {
            removeFromTabList(player);
            sendPacket(player, packet);
        }
    }

    public void leave(Player player) {
        leave(player, false);
    }

    public void leave() {
        leave(null, true);
    }

    private PacketPlayOutPlayerInfo packetAddTabList;
    private PacketPlayOutPlayerInfo.PlayerInfoData packetAddTabListData;
    private void addToTabList(Player player, boolean toAll) {
        PacketPlayOutPlayerInfo packet = packetAddTabList;
        PacketPlayOutPlayerInfo.PlayerInfoData data = packetAddTabListData;

        setValue(data, "d", gameProfile);
        List<PacketPlayOutPlayerInfo.PlayerInfoData> players = (List<PacketPlayOutPlayerInfo.PlayerInfoData>) getValue(packet, "b");
        players.add(data);
        setValue(packet, "a", PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER);
        setValue(packet, "b", players);

        if (toAll) {
            sendPacket(packet);
        } else {
            sendPacket(player, packet);
        }
    }

    public void addToTabList(Player player) {
        addToTabList(player, false);
    }

    public void addToTabList() {
        addToTabList(null, true);
    }

    private PacketPlayOutPlayerInfo packetRemoveTabList;
    private PacketPlayOutPlayerInfo.PlayerInfoData packetRemoveTabListData;
    private void removeFromTabList(Player player, boolean toAll) {
        PacketPlayOutPlayerInfo packet = packetRemoveTabList;
        PacketPlayOutPlayerInfo.PlayerInfoData data = packetRemoveTabListData;

        setValue(data, "d", gameProfile);
        List<PacketPlayOutPlayerInfo.PlayerInfoData> players = (List<PacketPlayOutPlayerInfo.PlayerInfoData>) getValue(packet, "b");
        players.add(data);
        setValue(packet, "a", PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER);
        setValue(packet, "b", players);

        if (toAll) {
            sendPacket(packet);
        } else {
            sendPacket(player, packet);
        }
    }

    public void removeFromTabList(Player player) {
        removeFromTabList(player, false);
    }

    public void removeFromTabList() {
        removeFromTabList(null, true);
    }

    public void setName(String name) {
        this.name = name;
        gameProfile = new GameProfile(UUID.randomUUID(), this.name);
    }

    public void changeSkin(Skin skin) {
        this.skin = skin;
        gameProfile = new GameProfile(UUID.randomUUID(), this.name);
        gameProfile.getProperties().put("textures", new Property("textures", skin.getValue(), skin.getSignature()));
    }

    public void changeSkin(UUID skinId) {
        Skin skin = instance.getNpcManager().getSkinManager().getSkin(skinId);
        if (skin != null)
            changeSkin(skin);
    }

    private PacketPlayOutEntityTeleport packetTeleport;
    private void teleport(Player player, Location location, boolean toAll) {
        this.location = location;
        PacketPlayOutEntityTeleport packet = packetTeleport;

        setValue(packet, "a", entityId);
        setValue(packet, "b", getFixLocation(location.getX()));
        setValue(packet, "c", getFixLocation(location.getY()));
        setValue(packet, "d", getFixLocation(location.getZ()));
        setValue(packet, "e", getFixRotation(location.getYaw()));
        setValue(packet, "f", getFixRotation(location.getPitch()));

        if (toAll) {
            sendPacket(packet);
            rotateHead(location.getYaw(), location.getPitch());
        } else {
            sendPacket(player, packet);
            rotateHead(player, location.getYaw(), location.getPitch());
        }
    }

    public void teleport(Player player, Location location) {
        teleport(player, location, false);
    }

    public void teleport(Location location) {
        teleport(null, location, true);
    }

    private PacketPlayOutEntity.PacketPlayOutEntityLook packetHeadLook;
    private PacketPlayOutEntityHeadRotation packetHeadRotation;
    private void rotateHead(Player player, float yaw, float pitch, boolean toAll) {
        packetHeadLook = new PacketPlayOutEntity.PacketPlayOutEntityLook(entityId, getFixRotation(yaw), getFixRotation(pitch), true);
        PacketPlayOutEntity.PacketPlayOutEntityLook lookPacket = packetHeadLook;
        PacketPlayOutEntityHeadRotation rotationPacket = packetHeadRotation;

        setValue(rotationPacket, "a", entityId);
        setValue(rotationPacket, "b", getFixRotation(yaw));

        if (toAll) {
            sendPacket(lookPacket);
            sendPacket(rotationPacket);
        } else {
            sendPacket(player, lookPacket);
            sendPacket(player, rotationPacket);
        }
    }

    public void rotateHead(Player player, float yaw, float pitch) {
        rotateHead(player, yaw, pitch, false);
    }

    public void rotateHead(float yaw, float pitch) {
        rotateHead(null, yaw, pitch, true);
    }

    private PacketPlayOutAnimation packetAnimation;
    private void animation(Player player, NpcAnimationType animationType, boolean toAll) {
        PacketPlayOutAnimation packet = packetAnimation;

        setValue(packet, "a", entityId);
        setValue(packet, "b", animationType.getId());

        if (toAll) {
            sendPacket(packet);
        } else {
            sendPacket(player, packet);
        }
    }

    public void animation(Player player, NpcAnimationType animationType) {
        animation(player, animationType, false);
    }

    public void animation(NpcAnimationType animationType) {
        animation(null, animationType, true);
    }

    private PacketPlayOutEntityStatus packetStatus;
    private void status(Player player, NpcStatusType statusType, boolean toAll) {
        PacketPlayOutEntityStatus packet = packetStatus;

        setValue(packet, "a", entityId);
        setValue(packet, "b", statusType.getId());

        if (toAll) {
            sendPacket(packet);
        } else {
            sendPacket(player, packet);
        }
    }

    public void status(Player player, NpcStatusType statusType) {
        status(player, statusType, false);
    }

    public void status(NpcStatusType statusType) {
        status(null, statusType, true);
    }

    private PacketPlayOutEntityEquipment packetEquip;
    private void equip(Player player, NpcSlotType slot, ItemStack itemStack, boolean toAll) {
        PacketPlayOutEntityEquipment packet = packetEquip;

        setValue(packet, "a", entityId);
        setValue(packet, "b", slot.getId());
        setValue(packet, "c", itemStack);

        if (toAll) {
            sendPacket(packet);
        } else {
            sendPacket(player, packet);
        }
    }

    public void equip(Player player, NpcSlotType slot, ItemStack itemStack) {
        equip(player, slot, itemStack, false);
    }

    public void equip(NpcSlotType slot, ItemStack itemStack) {
        equip(null, slot, itemStack, true);
    }

    private PacketPlayOutBed packetSleep;
    private void sleep(Player player, boolean state, boolean toAll) {
        if (state) {
            PacketPlayOutBed packet = packetSleep;
            setValue(packet, "a", entityId);
            setValue(packet, "b", new BlockPosition(bedLocation.getX(), bedLocation.getY(), bedLocation.getZ()));
            if (toAll) {
                for (Player players : Bukkit.getOnlinePlayers()) {
                    players.sendBlockChange(bedLocation, Material.BED_BLOCK, (byte) 0);
                }
                sendPacket(packet);
                teleport(location);
            } else {
                player.sendBlockChange(bedLocation, Material.BED_BLOCK, (byte) 0);
                sendPacket(player, packet);
                teleport(player, location);
            }
        } else {
            if (toAll) {
                animation(NpcAnimationType.LEAVE_BED);
                teleport(location);
            } else {
                animation(player, NpcAnimationType.LEAVE_BED);
                teleport(player, location);
            }
        }
    }

    public void sleep(Player player, boolean state) {
        sleep(player, state, false);
    }

    public void sleep(boolean state) {
        sleep(null, state, true);
    }

    private int getFixLocation(Double fixLocation) {
        return MathHelper.floor(fixLocation * 32);
    }

    private byte getFixRotation(float fixRotation) {
        return (byte) MathHelper.floor(fixRotation * 256 / 360);
    }

    public int getEntityId() {
        return entityId;
    }

    public UUID getNpcId() {
        return npcId;
    }

    public String getAlias() {
        return alias;
    }

    public String getName() {
        return name;
    }

    public Location getLocation() {
        return location;
    }

    public Skin getSkin() {
        return skin;
    }

    public boolean setAlias(String alias) {
        boolean successful = instance.getNpcManager().setNpcAlias(this, alias);
        if (successful)
            this.alias = alias;
        return successful;
    }
}
