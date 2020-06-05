package cn.lanink.hotpotato.command.base.adminsub;

import cn.lanink.hotpotato.command.base.BaseSubCommand;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParameter;

public class ReloadRoom extends BaseSubCommand {

    public ReloadRoom(String name) {
        super(name);
    }

    @Override
    public boolean canUser(CommandSender sender) {
        return sender.isOp();
    }

    @Override
    public String[] getAliases() {
        return new String[0];
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        hotPotato.reLoadRooms();
        sender.sendMessage("§a配置重载完成！请在后台查看信息！");
        return true;
    }

    @Override
    public CommandParameter[] getParameters() {
        return new CommandParameter[0];
    }

}
