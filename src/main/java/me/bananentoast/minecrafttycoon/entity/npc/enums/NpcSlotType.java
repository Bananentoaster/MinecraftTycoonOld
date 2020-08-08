package me.bananentoast.minecrafttycoon.entity.npc.enums;

public enum NpcSlotType {

    HAND(0),
    BOOTS(1),
    LEGGINGS(2),
    CHESTPLATE(3),
    HELMET(4);

    private int id;

    NpcSlotType(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

}
