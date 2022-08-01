package cn.lanink.hotpotato.room;

import cn.lanink.gamecore.utils.PlayerDataUtils;
import cn.lanink.gamecore.utils.Tips;
import cn.lanink.hotpotato.HotPotato;
import cn.lanink.hotpotato.tasks.WaitTask;
import cn.lanink.hotpotato.utils.Language;
import cn.lanink.hotpotato.utils.Tools;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.entity.data.Skin;
import cn.nukkit.level.Level;
import cn.nukkit.level.Position;
import cn.nukkit.utils.Config;
import lombok.Getter;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 房间类
 */
public class Room {

    protected final Language language = HotPotato.getInstance().getLanguage();

    public int waitTime;
    public int gameTime;

    protected RoomStatus status;
    protected String level;
    protected String waitSpawn;

    protected int setWaitTime;
    protected int setGameTime;

    @Getter
    protected int minPlayers;
    @Getter
    protected int maxPlayers;

    protected HashMap<Player, Integer> players = new HashMap<>();

    private final ArrayList<Position> randomSpawn = new ArrayList<>();

    private final LinkedHashMap<Player, Integer> skinNumber = new LinkedHashMap<>(); //玩家使用皮肤编号，用于防止重复使用
    private final LinkedHashMap<Player, Skin> skinCache = new LinkedHashMap<>(); //缓存玩家皮肤，用于退出房间时还原

    public Player victoryPlayer;
    public int roundCount;

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

        this.minPlayers = config.getInt("minPlayers", 3);
        if (this.minPlayers < 2) {
            this.minPlayers = 2;
        }
        this.maxPlayers = config.getInt("maxPlayers", 16);
        if (this.maxPlayers < this.minPlayers) {
            this.maxPlayers = this.minPlayers;
        }

        this.initData();

        this.status = RoomStatus.WAIT;
    }

    public RoomStatus getStatus() {
        return this.status;
    }

    /**
     * 设置房间状态
     * @param status 状态
     */
    public void setStatus(RoomStatus status) {
        this.status = status;
    }

    /**
     * 初始化房间数据
     */
    protected void initData() {
        this.waitTime = this.setWaitTime;
        this.gameTime = this.setGameTime;

        this.skinNumber.clear();
        this.skinCache.clear();

        this.victoryPlayer = null;

        this.roundCount = 0;
    }

    /**
     * 初始化Task
     */
    public void initTask() {
        this.setStatus(RoomStatus.WAIT_PLAYER);
        Server.getInstance().getScheduler().scheduleRepeatingTask(
                HotPotato.getInstance(), new WaitTask(HotPotato.getInstance(), this), 20);
    }

    /**
     * 结束本局游戏
     */
    public void endGame() {
        this.status = RoomStatus.WAIT;
        for (Player player : new ArrayList<>(this.getPlayers().keySet())) {
            this.quitRoom(player);
        }
        for (Player player : this.getLevel().getPlayers().values()) {
            //不要触发传送事件，防止某些弱智操作阻止我们！
            player.teleport(Server.getInstance().getDefaultLevel().getSafeSpawn(), null);
        }
        //因为某些原因无法正常传送走玩家，就全部踹出服务器！
        for (Player player : this.getLevel().getPlayers().values()) {
            player.kick("Teleport error!");
        }
        this.initData();
        Tools.cleanEntity(this.getLevel());
    }

    public boolean canJoin() {
        return (this.getStatus() == RoomStatus.WAIT || this.getStatus() == RoomStatus.WAIT_PLAYER) && this.getPlayers().size() < this.getMaxPlayers();
    }

    /**
     * 加入房间
     * @param player 玩家
     */
    public void joinRoom(Player player) {
        if (this.canJoin()) {
            if (this.status == RoomStatus.WAIT) {
                this.initTask();
            }
            this.addPlaying(player);
            Tools.rePlayerState(player, true);

            File file = new File(HotPotato.getInstance().getDataFolder() + "/PlayerInventory/" + player.getName() + ".json");
            PlayerDataUtils.PlayerData playerData = PlayerDataUtils.create(player);
            playerData.saveAll();
            playerData.saveToFile(file);

            player.getInventory().clearAll();
            player.getUIInventory().clearAll();

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
        this.players.remove(player);
        if (HotPotato.getInstance().isHasTips()) {
            Tips.removeTipsConfig(this.level, player);
        }

        Tools.rePlayerState(player, false);

        File file = new File(HotPotato.getInstance().getDataFolder() + "/PlayerInventory/" + player.getName() + ".json");
        if (file.exists()) {
            PlayerDataUtils.PlayerData playerData = PlayerDataUtils.create(player, file);
            if (file.delete()) {
                playerData.restoreAll();
            }
        }

        this.restoreSkin(player);
        HotPotato.getInstance().getIScoreboard().closeScoreboard(player);
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
