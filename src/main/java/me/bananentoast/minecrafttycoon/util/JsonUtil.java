package me.bananentoast.minecrafttycoon.util;

import me.bananentoast.minecrafttycoon.MinecraftTycoon;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.HashMap;

public class JsonUtil {

    MinecraftTycoon instance;

    public JsonUtil(MinecraftTycoon instance) {
        this.instance = instance;
    }

    public String getIdFromName(String name) {
        System.out.println("Get Id " + getJsonFromUrl("https://api.mojang.com/users/profiles/minecraft/" + name).get("id").toString() + " from name: " + name);
        return getJsonFromUrl("https://api.mojang.com/users/profiles/minecraft/" + name).get("id").toString();
    }

    public JSONObject getJsonFromUrl(String url) {
        try {
            InputStream inputStream = new URL(url).openStream();
            try {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, Charset.forName("UTF-8")));
                String jsonText = readAll(bufferedReader);
                JSONObject json = parseStringToJson(jsonText);
                return json;
            } finally {
                inputStream.close();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String readAll(Reader reader) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        int cp;
        while ((cp = reader.read()) != -1) {
            stringBuilder.append((char) cp);
        }
        return stringBuilder.toString();
    }

    private JSONObject parseStringToJson(String rawJson) throws ParseException {
        return (JSONObject) new JSONParser().parse(rawJson);
    }

    public String locationToString(Location location) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("world", location.getWorld().getName());
        jsonObject.put("x", location.getX());
        jsonObject.put("y", location.getY());
        jsonObject.put("z", location.getZ());
        jsonObject.put("yaw", location.getYaw());
        jsonObject.put("pitch", location.getPitch());
        return jsonObject.toString();
    }

    public Location stringToLocation(String string) {
        try {
            JSONObject jsonObject = (JSONObject) new JSONParser().parse(string);
            return new Location(Bukkit.getWorld(jsonObject.get("world").toString()), Double.valueOf(jsonObject.get("x").toString()), Double.valueOf(jsonObject.get("y").toString()), Double.valueOf(jsonObject.get("z").toString()), Float.valueOf(jsonObject.get("yaw").toString()), Float.valueOf(jsonObject.get("pitch").toString()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

}
