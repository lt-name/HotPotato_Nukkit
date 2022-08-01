package cn.lanink.hotpotato;

import cn.lanink.gamecore.scoreboard.ScoreboardUtil;
import cn.lanink.gamecore.scoreboard.base.IScoreboard;
import cn.lanink.hotpotato.command.AdminCommand;
import cn.lanink.hotpotato.command.UserCommand;
import cn.lanink.hotpotato.listener.HotPotatoListener;
import cn.lanink.hotpotato.listener.PlayerGameListener;
import cn.lanink.hotpotato.listener.PlayerJoinAndQuit;
import cn.lanink.hotpotato.listener.RoomLevelProtection;
import cn.lanink.hotpotato.player.PlayerDataManager;
import cn.lanink.hotpotato.room.Room;
import cn.lanink.hotpotato.utils.Language;
import cn.lanink.hotpotato.utils.MetricsLite;
import cn.lanink.hotpotato.utils.RankingManager;
import cn.lanink.hotpotato.utils.RsNpcXVariable;
import cn.nukkit.entity.data.Skin;
import cn.nukkit.level.Level;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.Config;
import com.smallaswater.npc.variable.VariableManage;
import lombok.Getter;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * HotPotato
 * @author lt_name
 */
public class HotPotato extends PluginBase {

    public static final String VERSION = "?";
    public static boolean debug = false;
    public static final Random RANDOM = new Random();
    private static HotPotato hotPotato;

    private Language language;
    private Config config;
    @Getter
    private Config rankingConfig;

    private final HashMap<String, Config> roomConfigs = new HashMap<>();
    private final LinkedHashMap<String, Room> rooms = new LinkedHashMap<>();

    private final LinkedHashMap<Integer, Skin> skins = new LinkedHashMap<>();

    private String cmdUser;
    private String cmdAdmin;
    @Getter
    private List<String> cmdWhitelist;
    private IScoreboard iScoreboard;

    private boolean hasTips = false;

    @Getter
    private String playerDataPath;

    public static HotPotato getInstance() {
        return hotPotato;
    }
    
    @Override
    public void onLoad() {
        hotPotato = this;

        this.playerDataPath = this.getDataFolder() + "/PlayerData/";
    
        File file1 = new File(this.getDataFolder() + "/Rooms");
        File file2 = new File(this.getDataFolder() + "/PlayerInventory");
        File file3 = new File(this.getDataFolder() + "/Skins");
        if (!file1.exists() && !file1.mkdirs()) {
            this.getLogger().error("Rooms 文件夹初始化失败");
        }
        if (!file2.exists() && !file2.mkdirs()) {
            this.getLogger().error("PlayerInventory 文件夹初始化失败");
        }
        if (!file3.exists() && !file3.mkdirs()) {
            this.getLogger().warning("Skins 文件夹初始化失败");
        }
        this.saveDefaultConfig();
        this.saveResource("RankingConfig.yml");
        this.config = new Config(this.getDataFolder() + "/config.yml", Config.YAML);
        this.rankingConfig = new Config(this.getDataFolder() + "/RankingConfig.yml", Config.YAML);
    }
    
    @Override
    public void onEnable() {
        this.getLogger().info("§e插件开始加载！");
        //加载计分板
        this.iScoreboard = ScoreboardUtil.getScoreboard();
        //检查Tips
        try {
            Class.forName("tip.Main");
            if (getServer().getPluginManager().getPlugin("Tips").isDisabled()) {
                throw new Exception("Not Loaded");
            }
            this.hasTips = true;
        } catch (Exception ignored) {

        }
        //语言文件
        saveResource("Language/zh_CN.yml");
        saveResource("Language/es_MX.yml");
        String s = this.config.getString("language", "zh_CN");
        File languageFile = new File(getDataFolder() + "/Language/" + s + ".yml");
        if (languageFile.exists()) {
            this.language = new Language(new Config(languageFile, Config.YAML));
            this.getLogger().info("§aLanguage: " + s + " loaded !");
        }else {
            this.language = new Language(new Config());
            this.getLogger().warning("§cLanguage: " + s + " Not found, Load the default language !");
        }

        PlayerDataManager.load();
        try {
            Class.forName("cn.lanink.rankingapi.RankingAPI");
            RankingManager.load();
        }catch (Exception ignored) {

        }

        
        this.loadRooms();
        
        this.getLogger().info("§e开始加载皮肤");
        this.loadSkins();
        
        this.cmdUser = this.config.getString("插件命令", "hotpotato");
        this.cmdAdmin = this.config.getString("管理命令", "hotpotatoadmin");
        this.cmdWhitelist = this.config.getStringList("cmdWhitelist");

        this.getServer().getCommandMap().register("", new UserCommand(this.cmdUser));
        this.getServer().getCommandMap().register("", new AdminCommand(this.cmdAdmin));

        this.getServer().getPluginManager().registerEvents(new PlayerJoinAndQuit(), this);
        this.getServer().getPluginManager().registerEvents(new RoomLevelProtection(), this);
        this.getServer().getPluginManager().registerEvents(new PlayerGameListener(this), this);
        this.getServer().getPluginManager().registerEvents(new HotPotatoListener(this), this);

        try {
            Class.forName("com.smallaswater.npc.variable.BaseVariableV2");
            VariableManage.addVariableV2("HotPotato", RsNpcXVariable.class);
        }catch (Exception ignored) {

        }

        try {
            new MetricsLite(this, 7464);
        } catch (Exception ignored) {
        
        }

        this.getLogger().info("§e插件加载完成！欢迎使用！");
    }

    @Override
    public void onDisable() {
        PlayerDataManager.save();

        if (!this.rooms.isEmpty()) {
            Iterator<Map.Entry<String, Room>> it = this.rooms.entrySet().iterator();
            while(it.hasNext()){
                Map.Entry<String, Room> entry = it.next();
                entry.getValue().endGame();
                this.getLogger().info("§c房间：" + entry.getKey() + " 已卸载！");
                it.remove();
            }
        }
        this.rooms.clear();
        this.roomConfigs.clear();
        this.getLogger().info("§c插件卸载完成！");
    }

    public IScoreboard getIScoreboard() {
        return this.iScoreboard;
    }

    public boolean isHasTips() {
        return this.hasTips;
    }

    public Language getLanguage() {
        return this.language;
    }

    @Override
    public Config getConfig() {
        return this.config;
    }

    public LinkedHashMap<String, Room> getRooms() {
        return this.rooms;
    }

    public Config getRoomConfig(Level level) {
        return getRoomConfig(level.getName());
    }

    private Config getRoomConfig(String level) {
        if (this.roomConfigs.containsKey(level)) {
            return this.roomConfigs.get(level);
        }
        Config config = new Config(getDataFolder() + "/Rooms/" + level + ".yml", Config.YAML);
        this.roomConfigs.put(level, config);
        return config;
    }

    public LinkedHashMap<Integer, Skin> getSkins() {
        return this.skins;
    }

    /**
     * 加载所有房间
     */
    private void loadRooms() {
        this.getLogger().info("§e开始加载房间");
        File[] s = new File(getDataFolder() + "/Rooms").listFiles();
        if (s != null) {
            for (File file1 : s) {
                String[] fileName = file1.getName().split("\\.");
                if (fileName.length > 0) {
                    Config config = getRoomConfig(fileName[0]);
                    if (config.getInt("waitTime", 0) == 0 ||
                            config.getInt("gameTime", 0) == 0 ||
                            "".equals(config.getString("waitSpawn", "").trim()) ||
                            config.getStringList("randomSpawn").size() == 0 ||
                            "".equals(config.getString("World", "").trim())) {
                        this.getLogger().warning("§c房间：" + fileName[0] + " 配置不完整，加载失败！");
                        continue;
                    }
                    Room room = new Room(config);
                    this.rooms.put(fileName[0], room);
                    this.getLogger().info("§a房间：" + fileName[0] + " 已加载！");
                }
            }
        }
        this.getLogger().info("§e房间加载完成！当前已加载 " + this.rooms.size() + " 个房间！");
    }

    /**
     * 卸载所有房间
     */
    public void unloadRooms() {
        if (this.rooms.values().size() > 0) {
            Iterator<Map.Entry<String, Room>> it = this.rooms.entrySet().iterator();
            while(it.hasNext()){
                Map.Entry<String, Room> entry = it.next();
                entry.getValue().endGame();
                getLogger().info("§c房间：" + entry.getKey() + " 已卸载！");
                it.remove();
            }
            this.rooms.clear();
        }
        if (this.roomConfigs.values().size() > 0) {
            this.roomConfigs.clear();
        }
    }

    /**
     * 重载所有房间
     */
    public void reLoadRooms() {
        this.unloadRooms();
        this.loadRooms();
    }

    /**
     * 加载所有皮肤
     */
    private void loadSkins() {
        File[] files = (new File(getDataFolder() + "/Skins")).listFiles();
        if (files != null && files.length > 0) {
            int x = 0;
            for (File file : files) {
                String skinName = file.getName();
                File skinFile = new File(getDataFolder() + "/Skins/" + skinName + "/skin.png");
                if (skinFile.exists()) {
                    Skin skin = new Skin();
                    BufferedImage skinData = null;
                    try {
                        skinData = ImageIO.read(skinFile);
                    } catch (IOException ignored) {
                        getLogger().warning(skinName + "加载失败，这可能不是一个正确的图片");
                    }
                    if (skinData != null) {
                        skin.setSkinData(skinData);
                        skin.setSkinId(skinName);
                        getLogger().info("§a编号: " + x + " 皮肤: " + skinName + " 已加载");
                        this.skins.put(x, skin);
                        x++;
                    }else {
                        getLogger().warning(skinName + "加载失败，这可能不是一个正确的图片");
                    }
                } else {
                    getLogger().warning(skinName + "加载失败，请将皮肤文件命名为 skin.png");
                }
            }
        }
        if (this.skins.size() > 15) {
            getLogger().info("§e皮肤加载完成！当前已加载 " + this.skins.size() + " 个皮肤！");
        }else {
            getLogger().warning("§c当前皮肤数量小于16，部分玩家仍可使用自己的皮肤");
        }
    }

    public String getCmdUser() {
        return this.cmdUser;
    }

    public String getCmdAdmin() {
        return this.cmdAdmin;
    }

}
