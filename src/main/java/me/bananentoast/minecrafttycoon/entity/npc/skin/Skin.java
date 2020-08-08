package me.bananentoast.minecrafttycoon.entity.npc.skin;

import me.bananentoast.minecrafttycoon.MinecraftTycoon;

import java.util.UUID;

public class Skin {

    private MinecraftTycoon instance;
    private UUID id;
    private String alias;
    private String value;
    private String signature;

    public Skin(MinecraftTycoon instance, UUID id, String value, String signature) {
        this.instance = instance;
        this.id = id;
        this.value = value;
        this.signature = signature;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public UUID getId() {
        return id;
    }

    public String getValue() {
        return value;
    }

    public String getSignature() {
        return signature;
    }

    public String getAlias() {
        return alias;
    }

    public boolean setAlias(String skinAlias) {
        boolean successful = instance.getNpcManager().getSkinManager().setSkinAlias(this, skinAlias);
        if (successful)
            this.alias = skinAlias;
        return successful;
    }

    @Override
    public String toString() {
        return "Skin{value=" + value + ", signature=" + signature + ", alias=" + alias + "}";
    }
}
