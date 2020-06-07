package cn.lanink.hotpotato.command;

import cn.lanink.hotpotato.command.base.BaseCommand;
import cn.lanink.hotpotato.command.adminsub.*;
import cn.nukkit.command.CommandSender;

public class AdminCommand extends BaseCommand {

    public AdminCommand(String name) {
        super(name, "HotPotato 管理命令");
        this.setPermission("HotPotato.command.admin");
        this.addSubCommand(new UiCommand("ui"));
        this.addSubCommand(new SetWaitSpawn("setwaitspawn"));
        this.addSubCommand(new AddRandomSpawn("addrandomspawn"));
        this.addSubCommand(new SetWaitTime("setwaittime"));
        this.addSubCommand(new SetGameTime("setgametime"));
        this.addSubCommand(new ReloadRoom("reloadroom"));
        this.addSubCommand(new UnloadRoom("unloadroom"));
        this.loadCommandBase();
    }

    @Override
    public void sendHelp(CommandSender sender) {
        sender.sendMessage(this.language.adminHelp.replace("%cmdName%", this.getName()));
    }

}
