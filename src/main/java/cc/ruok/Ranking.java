package cc.ruok;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.data.EntityMetadata;
import cn.nukkit.level.Level;
import cn.nukkit.network.protocol.AddEntityPacket;
import cn.nukkit.network.protocol.RemoveEntityPacket;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.*;

public class Ranking {

    private static final File FILE = new File(DSWB.getInstance().getDataFolder(), "ranking.properties");
    private static HashMap<String, String> ranking;
    private static String text;
    private static Level level;

    public static boolean put(String player, int score) {
        if (get(player) >= 0 && score >= get(player)) return false;
        ranking.put(player, String.valueOf(score));
        save();
        updateFloatingText();
        return true;
    }

    public static int get(String player) {
        if (ranking.get(player) == null) return -1;
        return Integer.parseInt(ranking.get(player));
    }

    public static void load() {
        level = Server.getInstance().getLevelByName(Game.getConfig().level);
        if (FILE.exists()) {
            try {
                Properties properties = new Properties();
                properties.load(new FileInputStream(FILE));
                ranking = new HashMap<>((Map) properties);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            ranking = new HashMap<>();
            save();
        }
    }

    public static void save() {
        try {
            Properties properties = new Properties();
            for (Map.Entry<String, String> entry : ranking.entrySet()) {
                properties.put(entry.getKey(), entry.getValue());
            }
            properties.store(new FileOutputStream(FILE), "Ranking");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 取分数最高的前10名并排序
     * @return 排行榜
     * @param <K> 玩家名称
     * @param <V> 分数
     */
    public static <K, V extends Comparable<? super V>> Map<String, Float> sort() {
        Comparator<Map.Entry<String, String>> valCmp = new Comparator<Map.Entry<String,String>>() {
            @Override
            public int compare(Map.Entry<String, String> o1, Map.Entry<String, String> o2) {
                return Integer.parseInt(o1.getValue()) - Integer.parseInt(o2.getValue());
            }
        };
        List<Map.Entry<String, String>> list = new ArrayList<>(ranking.entrySet());
        Collections.sort(list,valCmp);
        //输出map
        LinkedHashMap<String, Float> result = new LinkedHashMap<>();
        int sum = Math.min(list.size(), 10);
        for(int i = 0; i < sum; i++) {
            result.put(list.get(i).getKey(), (float) Integer.parseInt(list.get(i).getValue()) / 1000);
        }
        return result;
    }

    public static void updateFloatingText() {
        Config config = Game.getConfig();
        if (config.ranking == null) return;
        Map<String, Float> map = sort();
        int i = 0;
        StringBuilder name = new StringBuilder("§e[[ §6别踩白块 §c-- §d排行榜 §e]]\n");
        for (Map.Entry<String, Float> entry : map.entrySet()) {
            name.append("§aNo.").append(++i).append(" §l§e").append(entry.getKey()).append(" §f- §b").append(entry.getValue()).append("s\n");
        }
        name.append("§e[[ §6别踩白块 §c-- §d排行榜 §e]]");
        Ranking.text = name.toString();
        for (Player player : Server.getInstance().getOnlinePlayers().values()) {
            showFloatingText(player);
        }
    }

    public static void showFloatingText(Player player) {
        if (player.getLevel() != level) return;
        Config config = Game.getConfig();
        AddEntityPacket packet = new AddEntityPacket();
        packet.entityRuntimeId = config.rankingId;
        packet.entityUniqueId = config.rankingId;
        packet.type = 64;
        packet.yaw = 0;
        packet.headYaw = 0;
        packet.pitch = 0;
        packet.speedX = 0;
        packet.speedY = 0;
        packet.speedZ = 0;
        packet.x = (float) config.ranking.x;
        packet.y = (float) config.ranking.y;
        packet.z = (float) config.ranking.z;
        packet.metadata = new EntityMetadata()
                .putString(Entity.DATA_NAMETAG, text)
                .putBoolean(Entity.DATA_ALWAYS_SHOW_NAMETAG, true);
        player.dataPacket(packet);
    }

    public static void removeFloatingText(Player player) {
//            player.sendMessage("§l§a已移除排行榜.");
        RemoveEntityPacket pk = new RemoveEntityPacket();
        pk.eid = Game.getConfig().rankingId;
        player.dataPacket(pk);
    }

    public static void removeFloatingText() {
        for (Player player : Server.getInstance().getOnlinePlayers().values()) {
            removeFloatingText(player);
        }
    }

    public static Config.Seat setFloatingText(Player player) {
        Config.Seat seat = new Config.Seat();
        seat.x = player.getX();
        seat.y = player.getY() + 2;
        seat.z = player.getZ();
        seat.chunkX = player.getChunkX();
        seat.chunkZ = player.getChunkZ();
        return seat;
    }

    public static Level getLevel() {
        return level;
    }

    public static String getText() {
        return text;
    }


}
