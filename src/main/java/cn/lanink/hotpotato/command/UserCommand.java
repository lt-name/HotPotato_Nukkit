package cn.lanink.hotpotato.command;

import cn.lanink.hotpotato.command.base.BaseCommand;
import cn.lanink.hotpotato.command.usersub.Join;
import cn.lanink.hotpotato.command.usersub.List;
import cn.lanink.hotpotato.command.usersub.Quit;
import cn.lanink.hotpotato.ui.GuiCreate;
import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;

public class UserCommand extends BaseCommand {

    public UserCommand(String name) {
        super(name, "HotPotato 游戏命令");
        this.setPermission("HotPotato.command.user");
        this.addSubCommand(new Join("join"));
        this.addSubCommand(new Quit("quit"));
        this.addSubCommand(new List("list"));
        this.loadCommandBase();
    }

    @Override
    public void sendHelp(CommandSender sender) {
        sender.sendMessage(this.language.userHelp.replace("%cmdName%", this.getName()));
    }

    @Override
    public void sendUI(CommandSender sender) {
        GuiCreate.sendUserMenu((Player) sender);
    }

}
