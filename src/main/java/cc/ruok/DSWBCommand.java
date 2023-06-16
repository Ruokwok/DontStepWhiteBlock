package cc.ruok;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;

public class DSWBCommand extends Command {

    public DSWBCommand() {
        super("dswb", "别踩白块");
    }

    @Override
    public boolean execute(CommandSender sender, String s, String[] args) {
        if (args.length == 0) {
            if (!sender.isPlayer()) {
                sender.sendMessage("§c该指令不能以控制台执行!");
            } else {
                if (sender.isOp()) {
                    sender.sendMessage("§e[§别踩白块§e] §h查看帮助§f:");
                    sender.sendMessage("§a/dswb set 别踩白块设置");
                }
            }
            return true;
        }
        if (args[0].equals("set")) {
            if (sender.isPlayer() && sender.isOp()) {
                Setting.getInstance().setting((Player) sender);
                return true;
            } else {
                return false;
            }
        }
        return false;
    }
}
