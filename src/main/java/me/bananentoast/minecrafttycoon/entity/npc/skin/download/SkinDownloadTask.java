package me.bananentoast.minecrafttycoon.entity.npc.skin.download;

import me.bananentoast.minecrafttycoon.MinecraftTycoon;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class SkinDownloadTask extends Thread{

    private MinecraftTycoon instance;
    private String playerId;

    private String[] skinTextures = new String[] {"", ""};
    private boolean successful;

    public SkinDownloadTask(MinecraftTycoon instance, String playerId) {
        this.instance = instance;
        this.playerId = playerId;
    }

    @Override
    public synchronized void start() {
        super.start();
        JSONObject jsonPage = instance.getJsonUtil().getJsonFromUrl("https://sessionserver.mojang.com/session/minecraft/profile/" + playerId + "?unsigned=false");
        //System.out.println("Raw page: " + jsonPage.toString());
        JSONObject jsonProperties = (JSONObject) ((JSONArray) jsonPage.get("properties")).get(0);
        //System.out.println("Properties: " + jsonProperties.toString());
        //System.out.println("use Value: " + jsonProperties.get("value").toString());
        //System.out.println("use Signature: " + jsonProperties.get("signature").toString());
        skinTextures[0] = jsonProperties.get("value").toString();
        skinTextures[1] = jsonProperties.get("signature").toString();
        successful = true;
    }

    public boolean wasSuccessfully() {
        return successful;
    }

    public String[] getResult() {
        return skinTextures;
    }

    public void release() {
        try {
            this.finalize();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

}
