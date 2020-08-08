package me.bananentoast.minecrafttycoon.entity.npc.skin.download;

import me.bananentoast.minecrafttycoon.MinecraftTycoon;
import me.bananentoast.minecrafttycoon.entity.npc.skin.Skin;
import me.bananentoast.minecrafttycoon.entity.npc.skin.SkinManager;
import me.bananentoast.minecrafttycoon.util.Settings;

import java.util.HashMap;

public class SkinDownloader {

    private MinecraftTycoon instance;
    private SkinManager skinManager;

    private HashMap<String , SkinDownloadTask> currentDownloadTasks = new HashMap<>();

    public SkinDownloader(MinecraftTycoon instance, SkinManager skinManager) {
        this.instance = instance;
        this.skinManager = skinManager;
    }

    public synchronized Skin download(String playerId) {
        while (currentDownloadTasks.size() >= Settings.NPC_SKIN_DOWNLOAD_MAX.getSetting()) {}
        SkinDownloadTask downloadTask = new SkinDownloadTask(instance, playerId);
        currentDownloadTasks.put(playerId, downloadTask);
        downloadTask.start();
        if (downloadTask.wasSuccessfully()) {
            String[] skinTextures = downloadTask.getResult();
            Skin skin = new Skin(instance, skinManager.getNewSkinId(), skinTextures[0], skinTextures[1]);
            return skin;
        }
        currentDownloadTasks.remove(playerId);
        downloadTask.release();
        return null;
    }

    public Skin downloadFromName(String playerName) {
        return download(instance.getJsonUtil().getIdFromName(playerName));
    }

}
