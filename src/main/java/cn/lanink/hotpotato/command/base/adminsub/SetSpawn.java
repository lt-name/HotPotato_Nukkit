package cn.lanink.hotpotato.command.base.adminsub;

import cn.lanink.hotpotato.command.base.BaseSubCommand;
import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParameter;

public class SetSpawn extends BaseSubCommand {

    public SetSpawn(String name) {
        super(name);
    }

    @Override
    public boolean canUser(CommandSender sender) {
        return sender.isPlayer() && sender.isOp();
    }

    @Override
    public String[] getAliases() {
        return new String[0];
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        Player player = (Player) sender;
        hotPotato.roomSetSpawn(player, hotPotato.getRoomConfig(player.getLevel()));
        sender.sendMessage("§a等待点设置成功！");
        return true;
    }

    @Override
    public CommandParameter[] getParameters() {
        return new CommandParameter[0];
    }

}
