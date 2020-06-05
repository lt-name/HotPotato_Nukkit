package cn.lanink.hotpotato.utils;

import cn.nukkit.utils.Config;

public class Language {

    //命令
    public String useCmdInRoom = "§e >> §c游戏中无法使用其他命令";
    public String cmdHelp = "§a查看帮助：/%cmdName% help";
    public String userHelp = "§eGunWar--命令帮助 \n " +
            "§a/%cmdName% ui §e打开ui \n " +
            "§a/%cmdName% join 房间名称 §e加入游戏 \n " +
            "§a/%cmdName% quit §e退出游戏 \n " +
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
    public String adminHelp = "§eGunWar--命令帮助 \n " +
            "§a/%cmdName% ui §e打开ui \n " +
            "§a/%cmdName% setwaitspawn §e设置当前位置为等待点 \n " +
            "§a/%cmdName% setredspawn §e将当前位置设置为红队出生点 \n " +
            "§a/%cmdName% setbluespawn §e将当前位置设置为蓝队出生点 \n " +
            "§a/%cmdName% setwaittime 数字 §e设置游戏人数足够后的等待时间 \n " +
            "§a/%cmdName% setgametime 数字 §e设置每回合游戏最长时间 \n " +
            "§a/%cmdName% reloadroom §e重载所有房间 \n " +
            "§a/%cmdName% unloadroom §e关闭所有房间,并卸载配置";
    public String adminSetWaitSpawn = "§a等待出生点设置成功！";
    public String adminSetRedSpawn = "§a红队出生点设置成功！";
    public String adminSetBlueSpawn = "§a蓝队出生点设置成功！";
    public String adminNotNumber = "§a时间只能设置为正整数！";
    public String adminSetWaitTime = "§a等待时间已设置为：%time%";
    public String adminSetGameTime = "§a游戏时间已设置为：%time%";
    public String adminSetGameTimeShort = "§a游戏时间最小不能低于1分钟！";
    public String adminReload = "§a配置重载完成！请在后台查看信息！";
    public String adminUnload = "§a已卸载所有房间！请在后台查看信息！";

    public Language(Config config) {

    }
}
