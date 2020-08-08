package me.bananentoast.minecrafttycoon.entity.npc.enums;

public enum NpcStatusType {

    HURT((byte) 2),
    DEAD((byte) 3),
    EAT((byte) 9);

    private byte id;

    NpcStatusType(byte id) {
        this.id = id;
    }

    public byte getId() {
        return id;
    }

}
