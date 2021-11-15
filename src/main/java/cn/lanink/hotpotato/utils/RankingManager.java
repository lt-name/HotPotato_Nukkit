package cn.lanink.hotpotato.utils;

import cn.lanink.hotpotato.HotPotato;
import cn.lanink.hotpotato.player.PlayerDataManager;
import cn.lanink.rankingapi.Ranking;
import cn.lanink.rankingapi.RankingAPI;
import cn.lanink.rankingapi.RankingFormat;
import cn.nukkit.Server;
import cn.nukkit.level.Position;
import cn.nukkit.utils.Config;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 排行榜管理
 *
 * @author LT_Name
 */
public class RankingManager {

    private static final ArrayList<RankingData> RANKING_DATA_LIST = new ArrayList<>();
    private static final HashMap<String, Ranking> RANKING_MAP = new HashMap<>();

    /**
     * 加载排行榜
     */
    public static void load() {
        //读取排行榜数据
        Config rankingConfig = HotPotato.getInstance().getRankingConfig();
        RankingFormat rankingFormat = RankingFormat.getDefaultFormat();
        rankingFormat.setTop(rankingConfig.getString("RankingFormat.Top"));
        rankingFormat.setLine(rankingConfig.getString("RankingFormat.Line"));
        rankingFormat.setLineSelf(rankingConfig.getString("RankingFormat.LineSelf"));
        rankingFormat.setBottom(rankingConfig.getString("RankingFormat.Bottom"));

        List<Map> list = rankingConfig.getMapList("pos");
        for (Map map : list) {
            try {
                String levelName = (String) map.get("level");
                if (!Server.getInstance().loadLevel(levelName)) {
                    throw new RuntimeException("世界：" + levelName + " 加载失败！");
                }
                RANKING_DATA_LIST.add(
                        new RankingData(
                                (String) map.get("name"),
                                Position.fromObject(Tools.mapToVector3(map), Server.getInstance().getLevelByName(levelName))
                        )
                );
            } catch (Exception e) {
                HotPotato.getInstance().getLogger().error("读取排行榜数据出错！ 配置: " + map.toString(), e);
            }
        }
        if (HotPotato.debug) {
            HotPotato.getInstance().getLogger().info("[debug] " + RANKING_DATA_LIST);
        }
        //创建排行榜
        for (RankingData rankingData : RANKING_DATA_LIST) {
            Ranking ranking = RankingAPI.createRanking(HotPotato.getInstance(), rankingData.getName(), rankingData.getPosition());
            ranking.setRankingList(PlayerDataManager::getAllPlayerVictoryCount);
            ranking.setRankingFormat(rankingFormat);
            RANKING_MAP.put(rankingData.getName(), ranking);
        }
        HotPotato.getInstance().getLogger().info("排行榜加载完成！成功创建" + RANKING_MAP.size() + " 个排行榜！");
    }

    public static void addRanking(RankingData rankingData) {
        RANKING_DATA_LIST.add(rankingData);
    }

    /**
     * 保存排行榜数据
     */
    public static void save() {
        //TODO

    }

    @AllArgsConstructor
    @Data
    public static class RankingData {
        private String name;
        private Position position;
    }

}
