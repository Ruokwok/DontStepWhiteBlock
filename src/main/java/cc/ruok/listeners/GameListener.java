package cc.ruok.listeners;

import cc.ruok.Config;
import cc.ruok.Game;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.block.Block;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerInteractEvent;
import cn.nukkit.event.player.PlayerQuitEvent;

public class GameListener implements Listener {

    private Player player;
    private Config config;
    private Game game;
    boolean press = false;

    public GameListener(Player player, Game game) {
        this.player = player;
        this.game = game;
        config = Game.getConfig();
    }

    @EventHandler
    public void onClick(PlayerInteractEvent event) {
        if (event.getPlayer() == player) {
            if (press) return;
            press = true;
            Server.getInstance().getScheduler().scheduleDelayedTask(this::onPress, 3);
            Block block = event.getBlock();
            if (config.direction == 0 && block.y == config.originY && block.z == config.originZ && (
                    block.x >= config.originX - 3 && block.x <= config.originX
                    )) {
                if (block.getName().equals("Black Wool")) {
                    game.next();
                } else {
                    game.error();
                }
            } else if (config.direction == 1 && block.y == config.originY && block.x == config.originX && (
                    block.z >= config.originZ - 3 && block.z <= config.originZ
            )) {
                if (block.getName().equals("Black Wool")) {
                    game.next();
                } else {
                    game.error();
                }
            }
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        if (event.getPlayer() == player) {
            game.stop();
        }
    }

    public void onPress() {
        press = false;
    }

}
