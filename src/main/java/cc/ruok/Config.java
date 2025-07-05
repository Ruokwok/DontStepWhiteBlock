package cc.ruok;

import cn.nukkit.utils.Utils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.IOException;

public class Config {

    public String level = "world";
    public int originX = 0;
    public int originY = 0;
    public int originZ = 0;
    public int startX = 0;
    public int startY = 0;
    public int startZ = 0;
    public int line = 20;
    public int direction = 1;
    public Seat ranking;
    public long rankingId;
    public boolean ready = false;

    public static Config load(File file) {
        try {
            String json = Utils.readFile(file);
            return new Gson().fromJson(json, Config.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void save(File file) {
        try {
            file.createNewFile();
            String json = new GsonBuilder().setPrettyPrinting().create().toJson(this);
            Utils.writeFile(file, json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static class Seat {

        public double x = 0D;
        public double y = 0D;
        public double z = 0D;
        public int chunkX = 0;
        public int chunkZ = 0;

    }

}
