package cn.lanink.hotpotato.command;

import cn.lanink.hotpotato.command.base.BaseCommand;
import cn.lanink.hotpotato.command.base.adminsub.*;
import cn.nukkit.command.CommandSender;

public class AdminCommand extends BaseCommand {

    public AdminCommand(String name) {
        super(name, "HotPotato 管理命令");
        this.setPermission("HotPotato.command.admin");
        this.addSubCommand(new UnloadRoom("Ui"));
        this.addSubCommand(new SetSpawn("setspawn"));
        this.addSubCommand(new SetWaitTime("setwaittime"));
        this.addSubCommand(new SetGameTime("setgametime"));
        this.addSubCommand(new ReloadRoom("reloadroom"));
        this.addSubCommand(new UnloadRoom("unloadroom"));
        this.loadCommandBase();
    }

    @Override
    public void sendHelp(CommandSender sender) {
        sender.sendMessage("§eHotPotato--命令帮助");
        sender.sendMessage("§a/" + getName() + " Ui §e打开ui");
        sender.sendMessage("§a/" + getName() + " setspawn §e设置当前位置为出生点");
        sender.sendMessage("§a/" + getName() + " setwaittime 数字 §e设置游戏人数足够后的等待时间");
        sender.sendMessage("§a/" + getName() + " setgametime 数字 §e设置爆炸等待时间");
        sender.sendMessage("§a/" + getName() + " reloadroom §e重载所有房间");
        sender.sendMessage("§a/" + getName() + " unloadroom §e关闭所有房间,卸载配置");
    }

}
