package cc.ruok;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.scheduler.AsyncTask;

public class UpdateFloatingTextTask extends AsyncTask {
    @Override
    public void onRun() {
        if (!Game.getConfig().ready) return;
        Ranking.removeFloatingText();
        for (Player player : Server.getInstance().getOnlinePlayers().values()) {
            Ranking.showFloatingText(player);
        }
    }
}
