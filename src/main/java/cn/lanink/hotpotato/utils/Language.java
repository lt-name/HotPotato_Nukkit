package cn.lanink.hotpotato.utils;

import cn.nukkit.utils.Config;

public class Language {

    //命令
    public String useCmdInRoom = "§e >> §c游戏中无法使用其他命令";
    public String cmdHelp = "§a查看帮助：/%cmdName% help";
    public String userHelp = "§eHotPotato--命令帮助 \n" +
            "§a/%cmdName% §e打开ui \n" +
            "§a/%cmdName% join 房间名称 §e加入游戏 \n" +
            "§a/%cmdName% quit §e退出游戏 \n" +
            "§a/%cmdName% list §e查看房间列表";
    public String noPermission = "§c你没有权限使用这个命令！";
    public String joinRoom = "§a你已加入房间: %name%";
    public String joinRoomIsInRoom = "§c你已经在一个房间中了!";
    public String joinRoomIsRiding = "§a请勿在骑乘状态下进入房间！";
    public String joinRandomRoom = "§a已为你随机分配房间！";
    public String joinRoomIsPlaying = "§a该房间正在游戏中，请稍后";
    public String joinRoomIsFull = "§a该房间已满人，请稍后";
    public String joinRoomIsNotFound = "§a该房间不存在！";
    public String joinRoomNotAvailable = "§a暂无房间可用！";
    public String quitRoom = "§a你已退出房间";
    public String quitRoomNotInRoom = "§a你本来就不在游戏房间！";
    public String listRoom = "§e房间列表： §a %list%";
    public String useCmdInCon = "请不要在控制台执行此指令!";
    public String adminHelp = "§eHotPotato--命令帮助 \n" +
            "§a/%cmdName% §e打开ui \n" +
            "§a/%cmdName% setwaitspawn §e设置当前位置为等待出生点 \n" +
            "§a/%cmdName% addrandomspawn §e添加当前位置为随机出生点 \n" +
            "§a/%cmdName% setwaittime 数字 §e设置游戏人数达到最少人数的等待时间 \n" +
            "§a/%cmdName% setgametime 数字 §e设置爆炸时间 \n" +
            "§a/%cmdName% startroom §e强制开启所在地图的房间 \n" +
            "§a/%cmdName% stoproom §e强制关闭所在地图的房间 \n" +
            "§a/%cmdName% reloadroom §e重载所有房间 \n" +
            "§a/%cmdName% unloadroom §e关闭所有房间,并卸载配置";
    public String adminSetWaitSpawn = "§a等待出生点设置成功！";
    public String adminAddRandomSpawn = "§a随机出生点已添加, 当前已有 %number% 个随机出生点";
    public String adminNotNumber = "§a时间只能设置为正整数！";
    public String adminSetWaitTime = "§a等待时间已设置为：%time%";
    public String adminSetGameTime = "§a爆炸时间已设置为：%time%";
    public String adminSetGameTimeShort = "§a爆炸等待时间最小不能低于5秒！";
    public String adminStartRoom = "§a已强制开启游戏！";
    public String adminStartNoPlayer = "§a房间人数不足2人,无法开始游戏！";
    public String adminStartRoomIsPlaying = "§c房间已经开始了！";
    public String adminLevelNoRoom = "§a当前地图不是游戏房间！";
    public String adminStopRoom = "§a已强制结束房间！";
    public String adminReload = "§a配置重载完成！请在后台查看信息！";
    public String adminUnload = "§a已卸载所有房间！请在后台查看信息！";
    public String needRankName = "请输入排行榜名称！";
    public String rankNameRepeat = "已有名为：%name% 的排行榜了！";
    public String rankCreationSuccessful = "排行榜：%name% 创建成功！";
    public String rankDeleteSuccessful = "排行榜：%name% 删除成功！";
    public String rankNotFound = "未找到名称为 %name% 的排行榜！";
    //游戏内显示
    public String tpJoinRoomLevel = "§e >> §c要进入游戏地图，请先加入游戏！";
    public String tpQuitRoomLevel = "§e >> §c退出房间请使用命令！";
    public String scoreBoardTitle = "§e烫手山芋";
    public String waitTimeScoreBoard = " 玩家: §a%playerNumber%/16 \n §a开始倒计时: §e%time% ";
    public String waitScoreBoard = " 玩家: §a%playerNumber%/16 \n 最少游戏人数为 3 人 \n 等待玩家加入中 ";
    public String waitTimeBottom = "§a当前已有: %playerNumber% 位玩家 \n §a游戏还有: %time% 秒开始！";
    public String waitBottom = "§c等待玩家加入中,当前已有: %playerNumber% 位玩家";
    public String gameTimeScoreBoard = "§l§a爆炸时间:§e %time% 秒 \n §l§a存活人数: %playerNumber%";
    public String gameTimeBottom = "§l§a倒计时: §e %time% §a秒 \n §l§a存活人数: %playerNumber%";
    public String victoryMessage = "§e恭喜 %player% §e获得胜利";
    public String playerDeath = "§c %player% 爆炸了";
    //ui相关
    public String userMenuButton1 = "§e随机加入房间";
    public String userMenuButton2 = "§e退出当前房间";
    public String userMenuButton3 = "§e查看房间列表";
    public String adminMenuSetLevel = "当前设置地图：%name%";
    public String adminMenuButton1 = "§e设置等待出生点";
    public String adminMenuButton2 = "§e添加随机出生点";
    public String adminMenuButton3 = "§e设置时间参数";
    public String adminMenuButton4 = "§e重载所有房间";
    public String adminMenuButton5 = "§c卸载所有房间";
    public String adminTimeMenuInputText1 = "等待时间（秒）";
    public String adminTimeMenuInputText2 = "爆炸等待时间（秒）";
    public String joinRoomOK = "§l§a确认要加入房间: %name% §l§a？";
    public String buttonOK = "§a确定";
    public String buttonReturn = "§c返回";
    //物品
    public String itemQuitRoom = "§c退出房间";
    public String itemQuitRoomLore = "手持点击,即可退出房间";

    public Language(Config config) {
        this.useCmdInRoom = config.getString("useCmdInRoom", this.useCmdInRoom);
        this.cmdHelp = config.getString("cmdHelp", this.cmdHelp);
        this.userHelp = config.getString("userHelp", this.userHelp);
        this.noPermission = config.getString("noPermission", this.noPermission);
        this.joinRoom = config.getString("joinRoom", this.joinRoom);
        this.joinRoomIsInRoom = config.getString("joinRoomIsInRoom", this.joinRoomIsInRoom);
        this.joinRoomIsRiding = config.getString("joinRoomIsRiding", this.joinRoomIsRiding);
        this.joinRandomRoom = config.getString("joinRandomRoom", this.joinRandomRoom);
        this.joinRoomIsPlaying = config.getString("joinRoomIsPlaying", this.joinRoomIsPlaying);
        this.joinRoomIsFull = config.getString("joinRoomIsFull", this.joinRoomIsFull);
        this.joinRoomIsNotFound = config.getString("joinRoomIsNotFound", this.joinRoomIsNotFound);
        this.joinRoomNotAvailable = config.getString("joinRoomNotAvailable", this.joinRoomNotAvailable);
        this.quitRoom = config.getString("quitRoom", this.quitRoom);
        this.quitRoomNotInRoom = config.getString("quitRoomNotInRoom", this.quitRoomNotInRoom);
        this.listRoom = config.getString("listRoom", this.listRoom);
        this.useCmdInCon = config.getString("useCmdInCon", this.useCmdInCon);
        this.adminHelp = config.getString("adminHelp", this.adminHelp);
        this.adminSetWaitSpawn = config.getString("adminSetWaitSpawn", this.adminSetWaitSpawn);
        this.adminAddRandomSpawn = config.getString("adminAddRandomSpawn", this.adminAddRandomSpawn);
        this.adminNotNumber = config.getString("adminNotNumber", this.adminNotNumber);
        this.adminSetWaitTime = config.getString("adminSetWaitTime", this.adminSetWaitTime);
        this.adminSetGameTime = config.getString("adminSetGameTime", this.adminSetGameTime);
        this.adminSetGameTimeShort = config.getString("adminSetGameTimeShort", this.adminSetGameTimeShort);
        this.adminStartRoom = config.getString("adminStartRoom", this.adminStartRoom);
        this.adminStartNoPlayer = config.getString("adminStartNoPlayer", this.adminStartNoPlayer);
        this.adminStartRoomIsPlaying = config.getString("adminStartRoomIsPlaying", this.adminStartRoomIsPlaying);
        this.adminLevelNoRoom = config.getString("adminLevelNoRoom", this.adminLevelNoRoom);
        this.adminStopRoom = config.getString("adminStopRoom", this.adminStopRoom);
        this.adminReload = config.getString("adminReload", this.adminReload);
        this.adminUnload = config.getString("adminUnload", this.adminUnload);
        this.needRankName = config.getString("needRankName", this.needRankName);
        this.rankNameRepeat = config.getString("rankNameRepeat", this.rankNameRepeat);
        this.rankCreationSuccessful = config.getString("rankCreationSuccessful", this.rankCreationSuccessful);
        this.rankDeleteSuccessful = config.getString("rankDeleteSuccessful", this.rankDeleteSuccessful);
        this.rankNotFound = config.getString("rankNotFound", this.rankNotFound);
        this.tpJoinRoomLevel = config.getString("tpJoinRoomLevel", this.tpJoinRoomLevel);
        this.tpQuitRoomLevel = config.getString("tpQuitRoomLevel", this.tpQuitRoomLevel);
        this.scoreBoardTitle = config.getString("scoreBoardTitle", this.scoreBoardTitle);
        this.waitTimeScoreBoard = config.getString("waitTimeScoreBoard", this.waitTimeScoreBoard);
        this.waitScoreBoard = config.getString("waitScoreBoard", this.waitScoreBoard);
        this.waitTimeBottom = config.getString("waitTimeBottom", this.waitTimeBottom);
        this.waitBottom = config.getString("waitBottom", this.waitBottom);
        this.gameTimeScoreBoard = config.getString("gameTimeScoreBoard", this.gameTimeScoreBoard);
        this.gameTimeBottom = config.getString("gameTimeBottom", this.gameTimeBottom);
        this.victoryMessage = config.getString("victoryMessage", this.victoryMessage);
        this.playerDeath = config.getString("playerDeath", this.playerDeath);
        this.userMenuButton1 = config.getString("userMenuButton1", this.userMenuButton1);
        this.userMenuButton2 = config.getString("userMenuButton2", this.userMenuButton2);
        this.userMenuButton3 = config.getString("userMenuButton3", this.userMenuButton3);
        this.adminMenuSetLevel = config.getString("adminMenuSetLevel", this.adminMenuSetLevel);
        this.adminMenuButton1 = config.getString("adminMenuButton1", this.adminMenuButton1);
        this.adminMenuButton2 = config.getString("adminMenuButton2", this.adminMenuButton2);
        this.adminMenuButton3 = config.getString("adminMenuButton3", this.adminMenuButton3);
        this.adminMenuButton4 = config.getString("adminMenuButton4", this.adminMenuButton4);
        this.adminMenuButton5 = config.getString("adminMenuButton5", this.adminMenuButton5);
        this.adminTimeMenuInputText1 = config.getString("adminTimeMenuInputText1", this.adminTimeMenuInputText1);
        this.adminTimeMenuInputText2 = config.getString("adminTimeMenuInputText2", this.adminTimeMenuInputText2);
        this.joinRoomOK = config.getString("joinRoomOK", this.joinRoomOK);
        this.buttonOK = config.getString("buttonOK", this.buttonOK);
        this.buttonReturn = config.getString("buttonReturn", this.buttonReturn);
        //物品
        this.itemQuitRoom = config.getString("itemQuitRoom", this.itemQuitRoom);
        this.itemQuitRoomLore = config.getString("itemQuitRoomLore", this.itemQuitRoomLore);
    }

}
