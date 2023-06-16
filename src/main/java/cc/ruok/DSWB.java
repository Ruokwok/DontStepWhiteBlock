package cc.ruok;

import cc.ruok.listeners.BaseListener;
import cn.nukkit.plugin.PluginBase;

import java.io.File;

public class DSWB extends PluginBase {

    private static DSWB instance;
    private static File configFile;

    public static File getConfigFile() {
        return configFile;
    }

    @Override
    public void onLoad() {
        super.onLoad();
        getDataFolder().mkdir();
        instance = this;
        configFile = new File(getDataFolder(), "config.json");
    }

    @Override
    public void onEnable() {
        super.onEnable();
        getServer().getCommandMap().register("dswb", new DSWBCommand());
        if (!configFile.exists()) new Config().save(configFile);
        Game.loadConfig();
        Ranking.load();
        getServer().getPluginManager().registerEvents(new BaseListener(), this);
    }

    public static DSWB getInstance() {
        return instance;
    }
}
