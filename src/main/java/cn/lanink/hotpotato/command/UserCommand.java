package cn.lanink.hotpotato.command;

import cn.lanink.hotpotato.command.base.BaseCommand;
import cn.lanink.hotpotato.command.usersub.*;
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
        sender.sendMessage(this.language.userHelp.replace("%cmdName%", this.getName()));
    }

}
