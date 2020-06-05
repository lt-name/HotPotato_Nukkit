package cn.lanink.hotpotato.command;

import cn.lanink.hotpotato.command.base.BaseCommand;
import cn.lanink.hotpotato.command.base.usersub.Join;
import cn.lanink.hotpotato.command.base.usersub.List;
import cn.lanink.hotpotato.command.base.usersub.Quit;
import cn.lanink.hotpotato.command.base.usersub.Ui;
import cn.nukkit.command.CommandSender;

public class UserCommand extends BaseCommand {

    public UserCommand(String name) {
        super(name, "HotPotato 游戏命令");
        this.setPermission("HotPotato.command.user");
        this.addSubCommand(new Ui("ui"));
        this.addSubCommand(new Join("join"));
        this.addSubCommand(new Quit("quit"));
        this.addSubCommand(new List("list"));
        this.loadCommandBase();
    }

    @Override
    public void sendHelp(CommandSender sender) {
        sender.sendMessage("§eHotPotato--命令帮助");
        sender.sendMessage("§a/" + getName() + " ui §e打开ui");
        sender.sendMessage("§a/" + getName() + " join 房间名称 §e加入游戏");
        sender.sendMessage("§a/" + getName() + " quit §e退出游戏");
        sender.sendMessage("§a/" + getName() + " list §e查看房间列表");
    }

}
