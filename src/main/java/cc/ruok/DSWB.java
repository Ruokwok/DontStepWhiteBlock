package cc.ruok;

import cc.ruok.listeners.BaseListener;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.scheduler.TaskHandler;

import java.io.File;

public class DSWB extends PluginBase {

    private static DSWB instance;
    private static File configFile;
    public static Game game;
    private TaskHandler taskHandler;

    public static File getConfigFile() {
        return configFile;
    }

    @Override
    public void onLoad() {
        getDataFolder().mkdir();
        instance = this;
        configFile = new File(getDataFolder(), "config.json");
    }

    @Override
    public void onEnable() {
        getServer().getCommandMap().register("dswb", new DSWBCommand("dswb"));
        getServer().getCommandMap().register("bcbk", new DSWBCommand("bcbk"));
        if (!configFile.exists()) new Config().save(configFile);
        Game.loadConfig();
        Ranking.load();
        getServer().getPluginManager().registerEvents(new BaseListener(), this);
        if (Game.getConfig().ready) Ranking.updateFloatingText();
        taskHandler = getServer().getScheduler().scheduleRepeatingTask(this, new UpdateFloatingTextTask(), 6000);
    }

    @Override
    public void onDisable() {
        Ranking.save();
        Game.getConfig().save(configFile);
        if (taskHandler != null) taskHandler.cancel();
    }

    public static DSWB getInstance() {
        return instance;
    }
}
