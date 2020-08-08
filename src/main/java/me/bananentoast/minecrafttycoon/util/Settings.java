package me.bananentoast.minecrafttycoon.util;

public enum Settings {

    NPC_SKIN_DOWNLOAD_MAX(3);

    private Integer setting;

    Settings(Integer setting) {
        this.setting = setting;
    }

    public Integer getSetting() {
        return setting;
    }
}
