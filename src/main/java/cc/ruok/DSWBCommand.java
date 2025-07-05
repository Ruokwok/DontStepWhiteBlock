package cc.ruok;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.form.window.FormWindowSimple;

public class DSWBCommand extends Command {

    private String cmd;

    public DSWBCommand() {
        this("dswb");
    }

    public DSWBCommand(String cmd) {
        super(cmd, "别踩白块");
        this.cmd = cmd;
    }

    @Override
    public boolean execute(CommandSender sender, String s, String[] args) {
        if (args.length == 0) {
            if (!sender.isPlayer()) {
                sender.sendMessage("§c该指令不能以控制台执行!");
            } else {
                if (sender.isOp()) {
                    sender.sendMessage("§e[§别踩白块§e] §h查看帮助§f:");
                    sender.sendMessage("§a/" + cmd + " set 别踩白块设置");
                    sender.sendMessage("§a/" + cmd + " remove 移除别踩白块游戏");
                    sender.sendMessage("§a/" + cmd + " ranking 查看排行榜");
                }
            }
            return true;
        }
        if (args[0].equals("set")) {
            if (sender.isPlayer() && sender.isOp()) {
                Setting.getInstance().setting((Player) sender, cmd);
                return true;
            } else {
                return false;
            }
        }
        if (args[0].equals("stop")) {
            if (sender.isPlayer() && sender.isOp()) {
                if (DSWB.game != null && Game.getStatus() == 1) DSWB.game.error("§c§l强制结束.");
                return true;
            } else {
                return false;
            }
        }
        if (args[0].equals("remove")) {
            if (sender.isPlayer() && sender.isOp()) {
                Game.getConfig().ready = false;
                Game.getConfig().save(DSWB.getConfigFile());
                Ranking.removeFloatingText();
                return true;
            } else {
                return false;
            }
        }
        if (args[0].equals("ranking")) {
            if (sender.isPlayer()) {
                if (Game.getConfig().ready) {
                    String text = Ranking.getText();
                    Player player = (Player) sender;
                    FormWindowSimple windowSimple = new FormWindowSimple("别踩白块排行榜", text);
                    player.showFormWindow(windowSimple);
                    return true;
                }
            } else {
                return false;
            }
        }
        return false;
    }
}
