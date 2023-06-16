package cc.ruok.listeners;

import cc.ruok.Setting;
import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.block.BlockBreakEvent;
import cn.nukkit.level.Position;

public class SetListener implements Listener {

    private Player player;
    public Position p1;
    public Position p2;
    public Position ps;

    public SetListener(Player player) {
        this.player = player;
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        if (event.getPlayer() == player && Setting.getInstance().setStep == 2) {
            Thread runnable = new Thread(() -> {
                try {
                    Thread.sleep(50);
                    event.getBlock().getLevel().setBlock(event.getBlock(), event.getBlock());
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });
            runnable.start();
            if (p1 == null) {
                p1 = event.getBlock().getLocation().clone();
                player.sendMessage("§e第3步: §a请破坏最右下角的方块");
            } else if (p2 == null) {
                try {
                    if (event.getBlock().y != p1.y - 4 ||
                            (Math.abs(event.getBlock().x - p1.x) == 3
                                    && Math.abs(event.getBlock().z - p1.z) == 3) ||
                            (p1.x != event.getBlock().x && p1.z != event.getBlock().z)) {
                        player.sendMessage("§c操作有误，重置步骤!请检查画布尺寸是否正确(高5,宽4)");
                        Setting.getInstance().resetStep();
                    } else {
                        p2 = event.getBlock().getLocation();
                        player.sendMessage("§e第4步: §a请破坏一个方块将其指定为开始按钮");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                ps = event.getBlock().getLocation();
                Setting.getInstance().save(p1, p2, ps, player.getLevel().getName());
                player.sendMessage("§e第5步: §a请前往展示排行榜的位置，再次输入/dswb set");
                Setting.getInstance().setStep = 5;
            }
        }
    }

}
