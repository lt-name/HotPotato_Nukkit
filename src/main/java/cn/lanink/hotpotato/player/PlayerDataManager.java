package cn.lanink.hotpotato.player;

import cn.lanink.hotpotato.HotPotato;
import cn.nukkit.Player;
import cn.nukkit.utils.Config;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author LT_Name
 */
public class PlayerDataManager {

    private static final HashMap<String, PlayerData> PLAYER_DATA_MAP = new HashMap<>();

    private PlayerDataManager() {
        throw new RuntimeException("哎呀！你不能实例化这个类！");
    }

    public static void load() {
        File[] files = new File(HotPotato.getInstance().getPlayerDataPath()).listFiles();
        if (files == null || files.length == 0) {
            return;
        }
        files = Arrays.stream(files).filter(File::isDirectory).toArray(File[]::new);
        for (File file : files) {
            File[] files1 = file.listFiles();
            if (files1 == null || files1.length == 0) {
                continue;
            }
            Arrays.stream(files1)
                    .filter(File::isFile)
                    .filter(f -> f.getName().endsWith(".yml"))
                    .forEach(f -> {
                        String name = f.getName().split("\\.")[0];
                        PLAYER_DATA_MAP.put(
                                name,
                                new PlayerData(new Config(f, Config.YAML), name)
                        );
                    });
        }
    }

    public static void save() {
        for (PlayerData data : PLAYER_DATA_MAP.values()) {
            data.save();
        }
    }

    public static PlayerData getData(@NotNull Player player) {
        return getData(player.getName());
    }

    public static PlayerData getData(@NotNull String player) {
        if (!PLAYER_DATA_MAP.containsKey(player)) {
            String fileString = HotPotato.getInstance().getPlayerDataPath() +
                    player.substring(0,1).toLowerCase() + "/" + player + ".yml";
            Config config = new Config(fileString, Config.YAML);
            PlayerData value = new PlayerData(config, player);
            PLAYER_DATA_MAP.put(player, value);
        }
        return PLAYER_DATA_MAP.get(player);
    }

    /**
     * 获取所有玩家胜利次数
     *
     * @return 玩家胜利次数（Map）
     */
    public static Map<String, Integer> getAllPlayerVictoryCount() {
        Map<String, Integer> map = new LinkedHashMap<>();
        for (PlayerData playerData : PLAYER_DATA_MAP.values()) {
            map.put(playerData.getName(), playerData.getVictoryCount());
        }
        return map;
    }

}
