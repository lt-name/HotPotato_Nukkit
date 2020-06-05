package cn.lanink.hotpotato.room;

import cn.lanink.hotpotato.HotPotato;
import cn.lanink.hotpotato.tasks.TipsTask;
import cn.lanink.hotpotato.tasks.WaitTask;
import cn.lanink.hotpotato.utils.SavePlayerInventory;
import cn.lanink.hotpotato.utils.Tools;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.entity.data.Skin;
import cn.nukkit.level.Position;
import cn.nukkit.utils.Config;
import tip.messages.BossBarMessage;
import tip.messages.NameTagMessage;
import tip.utils.Api;

import java.util.*;

/**
 * 房间类
 */
public class Room extends BaseRoom {

    private LinkedHashMap<Player, Integer> skinNumber = new LinkedHashMap<>(); //玩家使用皮肤编号，用于防止重复使用
    private LinkedHashMap<Player, Skin> skinCache = new LinkedHashMap<>(); //缓存玩家皮肤，用于退出房间时还原
    public Player victoryPlayer;

    /**
     * 初始化
     * @param config 配置文件
     */
    public Room(Config config) {
        this.setWaitTime = config.getInt("等待时间", 120);
        this.setGameTime = config.getInt("游戏时间", 20);
        this.waitSpawn = config.getString("出生点", null);
        this.level = config.getString("World", null);
        this.initTime();
        if (this.getLevel() == null) {
            Server.getInstance().loadLevel(this.level);
        }
        this.mode = 0;
    }

    /**
     * 初始化Task
     */
    @Override
    public void initTask() {
        this.setMode(1);
        Server.getInstance().getScheduler().scheduleRepeatingTask(
                HotPotato.getInstance(), new WaitTask(HotPotato.getInstance(), this), 20, true);
        Server.getInstance().getScheduler().scheduleRepeatingTask(
                HotPotato.getInstance(), new TipsTask(HotPotato.getInstance(), this), 10);
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
            if (this.players.values().size() > 0 ) {
                this.players.keySet().forEach(this::quitRoomOnline);
                this.players.clear();
            }
        }else {
            this.getLevel().getPlayers().values().forEach(
                    player -> player.kick("\n§c房间非正常关闭!\n为了您的背包安全，请稍后重进服务器！"));
        }
        this.initTime();
        this.skinNumber.clear();
        this.skinCache.clear();
        this.task.clear();
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
            player.teleport(this.getSpawn());
            this.setRandomSkin(player, false);
            Tools.giveItem(player, 10);
            NameTagMessage nameTagMessage = new NameTagMessage(this.level, true, player.getName());
            Api.setPlayerShowMessage(player.getName(), nameTagMessage);
            BossBarMessage bossBarMessage = new BossBarMessage(this.level, false, 5, false, new LinkedList<>());
            Api.setPlayerShowMessage(player.getName(), bossBarMessage);
            player.sendMessage("你已加入房间: " + this.level);
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
        if (this.isPlaying(player)) {
            this.delPlaying(player);
        }
        if (online) {
            this.quitRoomOnline(player);
        }else {
            this.skinNumber.remove(player);
            this.skinCache.remove(player);
        }
    }

    @Override
    public void quitRoomOnline(Player player) {
        Tools.removePlayerShowMessage(this.level, player);
        player.teleport(Server.getInstance().getDefaultLevel().getSafeSpawn());
        Tools.rePlayerState(player, false);
        SavePlayerInventory.restore(player);
        this.setRandomSkin(player, true);
    }

    /**
     * 设置玩家随机皮肤
     * @param player 玩家
     * @param restore 是否为还原
     */
    public void setRandomSkin(Player player, boolean restore) {
        if (restore) {
            if (this.skinCache.containsKey(player)) {
                Tools.setPlayerSkin(player, this.skinCache.get(player));
                this.skinCache.remove(player);
            }
            this.skinNumber.remove(player);
        }else {
            for (Map.Entry<Integer, Skin> entry : HotPotato.getInstance().getSkins().entrySet()) {
                if (!this.skinNumber.containsValue(entry.getKey())) {
                    this.skinCache.put(player, player.getSkin());
                    this.skinNumber.put(player, entry.getKey());
                    Tools.setPlayerSkin(player, entry.getValue());
                    return;
                }
            }
        }
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
     * @return 出生点
     */
    public Position getSpawn() {
        return super.getWaitSpawn();
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
