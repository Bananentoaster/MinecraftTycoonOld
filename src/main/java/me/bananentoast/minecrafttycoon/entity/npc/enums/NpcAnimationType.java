package me.bananentoast.minecrafttycoon.entity.npc.enums;

public enum NpcAnimationType {

    ARM_SWING(0),
    TAKE_DAMAGE(1),
    LEAVE_BED(2),
    SWING_OFFHAND(3),
    CRIT_EFFECT(4),
    MAGIC_CRIT_EFFECT(5);

    private int id;

    NpcAnimationType(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

}
