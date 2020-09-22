package cn.lanink.hotpotato.room;

import cn.lanink.hotpotato.HotPotato;
import cn.lanink.hotpotato.tasks.WaitTask;
import cn.lanink.hotpotato.utils.SavePlayerInventory;
import cn.lanink.hotpotato.utils.Tips;
import cn.lanink.hotpotato.utils.Tools;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.entity.data.Skin;
import cn.nukkit.level.Position;
import cn.nukkit.utils.Config;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 房间类
 */
public class Room extends BaseRoom {

    private final ArrayList<Position> randomSpawn = new ArrayList<>();
    private final LinkedHashMap<Player, Integer> skinNumber = new LinkedHashMap<>(); //玩家使用皮肤编号，用于防止重复使用
    private final LinkedHashMap<Player, Skin> skinCache = new LinkedHashMap<>(); //缓存玩家皮肤，用于退出房间时还原
    public Player victoryPlayer;

    /**
     * 初始化
     * @param config 配置文件
     */
    public Room(Config config) {
        this.setWaitTime = config.getInt("waitTime", 120);
        this.setGameTime = config.getInt("gameTime", 20);
        this.waitSpawn = config.getString("waitSpawn", null);
        this.level = config.getString("World", null);
        if (this.getLevel() == null) {
            Server.getInstance().loadLevel(this.level);
        }
        for (String string : config.getStringList("randomSpawn")) {
            String[] s = string.split(":");
            this.randomSpawn.add(new Position(
                    Integer.parseInt(s[0]),
                    Integer.parseInt(s[1]),
                    Integer.parseInt(s[2]),
                    this.getLevel()));
        }
        this.initTime();
        this.mode = 0;
    }

    /**
     * 初始化Task
     */
    @Override
    public void initTask() {
        this.setMode(1);
        Server.getInstance().getScheduler().scheduleRepeatingTask(
                HotPotato.getInstance(), new WaitTask(HotPotato.getInstance(), this), 20);
    }

    /**
     * 结束本局游戏
     */
    @Override
    public void endGame() {
        this.endGame(true);
    }

    /**
     * 结束本局游戏
     * @param normal 正常关闭
     */
    public void endGame(boolean normal) {
        this.mode = 0;
        if (normal) {
            if (this.players.size() > 0 ) {
                Iterator<Map.Entry<Player, Integer>> it = this.players.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry<Player, Integer> entry = it.next();
                    it.remove();
                    this.quitRoom(entry.getKey());
                }
            }
        }else {
            this.getLevel().getPlayers().values().forEach(
                    player -> player.kick(HotPotato.getInstance().getLanguage().roomSafeKick));
        }
        this.initTime();
        this.skinNumber.clear();
        this.skinCache.clear();
        this.victoryPlayer = null;
        Tools.cleanEntity(this.getLevel());
    }

    /**
     * 加入房间
     * @param player 玩家
     */
    @Override
    public void joinRoom(Player player) {
        if (this.players.values().size() < 16) {
            if (this.mode == 0) {
                this.initTask();
            }
            this.addPlaying(player);
            Tools.rePlayerState(player, true);
            SavePlayerInventory.save(player);
            player.teleport(this.getWaitSpawn());
            this.setRandomSkin(player);
            Tools.giveItem(player, 10);
            if (HotPotato.getInstance().isHasTips()) {
                Tips.closeTipsShow(this.level, player);
            }
        }
    }

    /**
     * 退出房间
     * @param player 玩家
     */
    public void quitRoom(Player player) {
        this.quitRoom(player, true);
    }

    /**
     * 退出房间
     * @param player 玩家
     * @param online 是否在线
     */
    @Override
    public void quitRoom(Player player, boolean online) {
        this.players.remove(player);
        player.teleport(Server.getInstance().getDefaultLevel().getSafeSpawn());
        Tools.rePlayerState(player, false);
        SavePlayerInventory.restore(player);
        this.restoreSkin(player);
        HotPotato.getInstance().getIScoreboard().closeScoreboard(player);
        if (HotPotato.getInstance().isHasTips()) {
            Tips.removeTipsConfig(this.level, player);
        }
    }

    /**
     * 为玩家设置随机皮肤
     * @param player 玩家
     */
    public void setRandomSkin(Player player) {
        for (Map.Entry<Integer, Skin> entry : HotPotato.getInstance().getSkins().entrySet()) {
            if (!this.skinNumber.containsValue(entry.getKey())) {
                this.skinCache.put(player, player.getSkin());
                this.skinNumber.put(player, entry.getKey());
                Tools.setPlayerSkin(player, entry.getValue());
                return;
            }
        }
    }

    /**
     * 还原玩家皮肤
     * @param player 玩家
     */
    public void restoreSkin(Player player) {
        if (this.skinCache.containsKey(player)) {
            Tools.setPlayerSkin(player, this.skinCache.get(player));
            this.skinCache.remove(player);
        }
        this.skinNumber.remove(player);
    }

    /**
     * 记录在游戏内的玩家
     * @param player 玩家
     */
    public void addPlaying(Player player) {
        if (!this.players.containsKey(player)) {
            this.addPlaying(player, 0);
        }
    }

    /**
     * 记录在游戏内的玩家
     * @param player 玩家
     * @param mode 身份
     */
    public void addPlaying(Player player, Integer mode) {
        this.players.put(player, mode);
    }

    /**
     * 删除记录
     * @param player 玩家
     */
    private void delPlaying(Player player) {
        this.players.remove(player);
    }

    /**
     * 获取随机出生点
     * @return 随机出生点列表
     */
    public ArrayList<Position> getRandomSpawn() {
        return this.randomSpawn;
    }

    /**
     * 获取玩家在游戏中使用的皮肤
     * @param player 玩家
     * @return 皮肤
     */
    public Skin getPlayerSkin(Player player) {
        if (this.skinNumber.containsKey(player)) {
            return HotPotato.getInstance().getSkins().get(this.skinNumber.get(player));
        }
        return player.getSkin();
    }

}
