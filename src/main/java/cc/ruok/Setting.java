package cc.ruok;

import cc.ruok.listeners.SetListener;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.event.HandlerList;
import cn.nukkit.event.Listener;
import cn.nukkit.form.element.ElementInput;
import cn.nukkit.form.element.ElementLabel;
import cn.nukkit.form.window.FormWindowCustom;
import cn.nukkit.form.window.FormWindowModal;
import cn.nukkit.level.Position;

public class Setting {

    private Player player;
    public byte setStep = 0;
    private Listener listener;
    private Position p1;
    private Position p2;
    private Position ps;
    private String level;
    public int line = 20;
    private Config.Seat seat;

    public static Setting setting = new Setting();

    public static Setting getInstance() {
        return setting;
    }

    private Setting() {
    }

    public void setting(Player player) {
        if (setStep == 0) {
            this.player = player;
            FormWindowModal window = new FormWindowModal("别踩白块",
                    "§e第1步:\n" +
                            "§a请建造一个 高5 宽4 的结构作为游戏画布\n" +
                            "§c*§b可使用任意方块\n" +
                            "§c*§b可位于x轴或z轴平面\n" +
                            "§f建造完成后请再次使用§a/dswb set§f命令进行下一步的设置", "知道了", "关闭");
            player.showFormWindow(window);
            setStep = 1;
        } else if (setStep == 1) {
            setStep = 2;
            listener = new SetListener(player);
            Server.getInstance().getPluginManager().registerEvents(listener, DSWB.getInstance());
            player.sendMessage("§e第2步: §a请破坏最左上角的方块");
        } else if (setStep == 5) {
            seat = Ranking.setFloatingText(player);
            Setting.getInstance().setLine();
        }
    }

    public void resetStep() {
        setStep = 0;
        if (listener != null) HandlerList.unregisterAll(listener);
    }

    public void save(Position p1, Position p2, Position ps, String level) {
        this.p1 = p1;
        this.p2 = p2;
        this.ps = ps;
        this.level = level;
    }

    public void setLine() {
        FormWindowCustom window = new FormWindowCustom("别踩白块");
        window.addElement(new ElementLabel("§e第2步: §a请设置游戏步长"));
        window.addElement(new ElementInput("最低为5", "最低为5", "20"));
        player.showFormWindow(window, 1145146688);
    }

    public void finish() {
        resetStep();
        player.sendMessage("§e设置完成!§b点击开始方块即可游玩。");
        Config config = new Config();
        config.level = level;
        config.line = line;
        config.ranking = seat;
        config.startX = (int) ps.x;
        config.startY = (int) ps.y;
        config.startZ = (int) ps.z;
        config.originY = (int) p1.y - 4;
        if (p1.z == p2.z) {
            config.direction = 0;
            if (p1.x > p2.x) {
                config.originX = (int) p1.x;
                config.originZ = (int) p1.z;
            } else {
                config.originX = (int) p2.x;
                config.originZ = (int) p2.z;
            }
        } else {
            config.direction = 1;
            if (p1.z > p2.z) {
                config.originX = (int) p1.x;
                config.originZ = (int) p1.z;
            } else {
                config.originX = (int) p2.x;
                config.originZ = (int) p2.z;
            }
        }
        config.save(DSWB.getConfigFile());
        Game.loadConfig();
        new Game().stop();
        Ranking.updateFloatingText();
    }

}
