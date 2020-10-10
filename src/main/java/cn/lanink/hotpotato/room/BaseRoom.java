package cn.lanink.hotpotato.room;

import cn.lanink.hotpotato.HotPotato;
import cn.lanink.hotpotato.utils.Language;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.level.Level;
import cn.nukkit.level.Position;

import java.util.HashMap;

/**
 * 基础/通用 房间类
 * @author lt_name
 */
public abstract class BaseRoom {

    protected final Language language = HotPotato.getInstance().getLanguage();
    public int waitTime, gameTime;
    protected int mode; //0未初始化 1等待 2游戏 3胜利结算
    protected String level, waitSpawn;
    protected int setWaitTime, setGameTime;
    protected HashMap<Player, Integer> players = new HashMap<>();

    /**
     * 初始化task
     */
    protected abstract void initTask();

    /**
     * 初始化倒计时时间
     */
    protected void initTime() {
        this.waitTime = this.setWaitTime;
        this.gameTime = this.setGameTime;
    }

    public int getMode() {
        return this.mode;
    }

    /**
     * 设置房间状态
     * @param mode 状态
     */
    public void setMode(int mode) {
        this.mode = mode;
    }

    /**
     * 结束房间
     */
    public abstract void endGame();

    /**
     * 加入房间
     * @param player 玩家
     */
    public abstract void joinRoom(Player player);

    /**
     * 退出房间
     * @param player 玩家
     * @param online 是否在线
     */
    public void quitRoom(Player player, boolean online) {
        this.players.remove(player);
    }

    /**
     * 获取玩家是否在房间内
     * @param player 玩家
     * @return 是否在房间
     */
    public boolean isPlaying(Player player) {
        return this.players.containsKey(player);
    }

    /**
     * 获取玩家列表
     * @return 玩家列表
     */
    public HashMap<Player, Integer> getPlayers() {
        return this.players;
    }

    /**
     * 获取玩家身份
     * @param player 玩家
     * @return 玩家身份
     */
    public int getPlayerMode(Player player) {
        if (this.isPlaying(player)) {
            return this.players.get(player);
        }
        return 0;
    }

    /**
     * 获取设置的等待时间
     * @return 等待时间
     */
    public int getSetWaitTime() {
        return this.setWaitTime;
    }

    /**
     * 获取设置的游戏时间
     * @return 游戏时间
     */
    public int getSetGameTime() {
        return this.setGameTime;
    }

    /**
     * 获取世界
     * @return 世界
     */
    public Level getLevel() {
        return Server.getInstance().getLevelByName(this.level);
    }

    /**
     * 获取等待出生点
     * @return 出生点
     */
    public Position getWaitSpawn() {
        String[] s = this.waitSpawn.split(":");
        return new Position(Integer.parseInt(s[0]),
                Integer.parseInt(s[1]),
                Integer.parseInt(s[2]),
                this.getLevel());
    }

}
