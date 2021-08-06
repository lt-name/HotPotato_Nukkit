package cn.lanink.hotpotato.command;

import cn.lanink.hotpotato.command.adminsub.*;
import cn.lanink.hotpotato.command.base.BaseCommand;
import cn.lanink.hotpotato.ui.GuiCreate;
import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;

public class AdminCommand extends BaseCommand {

    public AdminCommand(String name) {
        super(name, "HotPotato 管理命令");
        this.setPermission("HotPotato.command.admin");

        this.addSubCommand(new SetWaitSpawn("SetWaitSpawn"));
        this.addSubCommand(new AddRandomSpawn("AddRandomSpawn"));
        this.addSubCommand(new SetWaitTime("SetWaitTime"));
        this.addSubCommand(new SetGameTime("SetGameTime"));
        this.addSubCommand(new SetMinPlayers("SetMinPlayers"));
        this.addSubCommand(new SetMaxPlayers("SetMaxPlayers"));

        this.addSubCommand(new StartRoom("StartRoom"));
        this.addSubCommand(new StopRoom("StopRoom"));
        this.addSubCommand(new ReloadRoom("ReloadRoom"));
        this.addSubCommand(new UnloadRoom("UnloadRoom"));

        this.loadCommandBase();
    }

    @Override
    public void sendHelp(CommandSender sender) {
        sender.sendMessage(this.language.adminHelp.replace("%cmdName%", this.getName()));
    }

    @Override
    public void sendUI(CommandSender sender) {
        GuiCreate.sendAdminMenu((Player) sender);
    }

}
