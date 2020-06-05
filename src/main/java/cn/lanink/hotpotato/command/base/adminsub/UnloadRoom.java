package cn.lanink.hotpotato.command.base.adminsub;

import cn.lanink.hotpotato.command.base.BaseSubCommand;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParameter;

public class UnloadRoom extends BaseSubCommand {

    public UnloadRoom(String name) {
        super(name);
    }

    @Override
    public boolean canUser(CommandSender sender) {
        return false;
    }

    @Override
    public String[] getAliases() {
        return new String[0];
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        hotPotato.unloadRooms();
        sender.sendMessage("§a已卸载所有房间！请在后台查看信息！");
        return true;
    }

    @Override
    public CommandParameter[] getParameters() {
        return new CommandParameter[0];
    }

}
