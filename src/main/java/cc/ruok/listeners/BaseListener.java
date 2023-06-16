package cc.ruok.listeners;

import cc.ruok.*;
import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.level.ChunkLoadEvent;
import cn.nukkit.event.player.PlayerFormRespondedEvent;
import cn.nukkit.event.player.PlayerInteractEvent;
import cn.nukkit.event.player.PlayerJoinEvent;
import cn.nukkit.event.player.PlayerQuitEvent;
import cn.nukkit.form.response.FormResponseCustom;

import java.util.HashMap;

public class BaseListener implements Listener {

    @EventHandler
    public void onClick(PlayerInteractEvent event) {
        Block block = event.getBlock();
//        event.getPlayer().sendMessage("x"+block.x + " y"+ block.y+" z"+block.z);

        Config config = Game.getConfig();
        if (config == null) return;
        if (block.x == config.startX && block.y == config.startY && block.z == config.startZ) {
            Game.start(event.getPlayer());
        }
    }

    @EventHandler
    public void onForm(PlayerFormRespondedEvent event) {
        if (event.getFormID() == 1145146688) {
            FormResponseCustom response = (FormResponseCustom) event.getResponse();
            if (response == null) return;
            HashMap<Integer, Object> map = response.getResponses();
            String l = (String) map.get(1);
            try {
                int line = Integer.parseInt(l);
                if (line >= 5) {
                    Setting.getInstance().line = line;
                    Setting.getInstance().finish();
                } else {
                    event.getPlayer().sendMessage("§c输入有误!重置步骤");
                    Setting.getInstance().resetStep();
                }
            } catch (Exception e) {
                event.getPlayer().sendMessage("§c输入有误!重置步骤");
                Setting.getInstance().resetStep();
                e.printStackTrace();
            }
        }
    }

    @EventHandler
    public void onChunkLoad(ChunkLoadEvent event) {
        Config config = Game.getConfig();
        if (config!= null && config.ranking != null) {
            if (event.getChunk().getX() == config.ranking.chunkX && event.getChunk().getZ() == config.ranking.chunkZ) {
                Ranking.updateFloatingText();
            }
        }
    }

}
