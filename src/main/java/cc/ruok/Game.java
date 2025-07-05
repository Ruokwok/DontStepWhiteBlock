package cc.ruok;

import cc.ruok.listeners.GameListener;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.block.Block;
import cn.nukkit.event.HandlerList;
import cn.nukkit.level.Level;
import cn.nukkit.level.Position;
import cn.nukkit.level.Sound;
import cn.nukkit.scheduler.Task;

import java.util.ArrayList;
import java.util.Collections;

public class Game {

    private static int status = 0;
    private static Config config;
    private static Level level;
    private ArrayList<Integer> list;
    private Player player;
    private int step = 0;
    private GameListener listener;
    private long start;
    private static Task task;

    public static Game start(Player player) {
        if (status != 0) {
            player.sendMessage("游戏正在进行，请等待。");
            return null;
        }
        Game game = new Game();
        status = 1;
        game.player = player;
        level = Server.getInstance().getLevelByName(config.level);
        game.list = mkList(config.line);
        game.listener = new GameListener(player, game);
        Server.getInstance().getPluginManager().registerEvents(game.listener, DSWB.getInstance());
        game.show();
        game.start = System.currentTimeMillis();
        task = new Task() {
            @Override
            public void onRun(int i) {
                game.error("操作超时，游戏结束!");
            }
        };
        Server.getInstance().getScheduler().scheduleDelayedTask(DSWB.getInstance(), task, config.line * 40);
        return game;
    }

    public void cancelTask() {
        if (task == null) return;
        Server.getInstance().getScheduler().cancelTask(task.getTaskId());
    }

    public static void loadConfig() {
        config = Config.load(DSWB.getConfigFile());
        Game.level = Server.getInstance().getLevelByName(config.level);
    }

    public void show() {
        for (int y = 0; y < 5; y++) {
            for (int x = 0; x < 4; x++) {
                Position pos = getPosition(x, y);
                if (pos != null) {
                    int color;
                    try {
                        color = x == list.get(step + y) ? 15 : 0;
                    } catch (Exception e) {
                        color = 5;
                    }
                    getLevel().setBlock(pos, Block.get(35, color));
                }
            }
        }
    }

    public void finish() {
        long time = System.currentTimeMillis() - start;
        float t = (float) time / 1000;
        Server.getInstance().broadcastMessage("§e" + player.getName() + "§a§l完成了别踩白块游戏!§b用时:" + t + "s");
        Ranking.put(player.getName(), (int) time);
//        Ranking.updateFloatingText();
    }

    public void next() {
        if (step == list.size() - 1) {
            finish();
            HandlerList.unregisterAll(listener);
            Server.getInstance().getScheduler().scheduleDelayedTask(DSWB.getInstance(), new Task() {
                @Override
                public void onRun(int i) {
                    stop();
                }
            }, 40);
        }
        step++;
        show();
        getLevel().addSound(player, Sound.NOTE_HARP, 1, (float) (2.0F / list.size()) * step);
    }

    public void error() {
        error("§c§l失败! 游戏结束.");
    }

    public void error(String info) {
        player.sendMessage(info);
        getLevel().addSound(player, Sound.ITEM_TRIDENT_THUNDER);
        int x = 0;
        int y = 0;
        for (int i = 0; i < 20; i++) {
            Position pos = getPosition(x, y);
            if (pos != null) getLevel().setBlock(pos, Block.get(35, 14));
            x++;
            if (x > 3) {
                x = 0;
                y++;
            }
        }
        HandlerList.unregisterAll(listener);
        Server.getInstance().getScheduler().scheduleDelayedTask(DSWB.getInstance(), new Task() {
            @Override
            public void onRun(int i) {
                stop();
            }
        }, 40);
    }

    public void stop() {
        cancelTask();
        int x = 0;
        int y = 0;
        boolean color = true;
        for (int i = 0; i < 20; i++) {
            Position pos = getPosition(x, y);
            if (pos != null) getLevel().setBlock(pos, Block.get(35, color ? 0 : 15));
            x++;
            color = !color;
            if (x > 3) {
                x = 0;
                y++;
                color = !color;
            }
        }
        status = 0;
    }

    public static ArrayList<Integer> mkList(int line) {
        int pos = 0;
        ArrayList<Integer> list = new ArrayList<>();
        for (int i = 0; i < line; i++) {
            if (pos >= 4) pos = 0;
            list.add(pos);
            pos++;
        }
        Collections.shuffle(list);
        return list;
    }

    public static Position getPosition(int x, int y) {
        if (x < 0 || x > 3) return null;
        if (y < 0 || y > 4) return null;
        if (config.direction == 0) {
            return new Position(config.originX - x, config.originY + y, config.originZ, level);
        } else {
            return new Position(config.originX, config.originY + y, config.originZ - x, level);
        }
    }

    public static Config getConfig() {
        return config;
    }

    public static Level getLevel() {
        return level;
    }

    public Player getPlayer() {
        return player;
    }

    public static int getStatus() {
        return status;
    }
}
