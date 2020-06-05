package cn.lanink.hotpotato.command.base.adminsub;

import cn.lanink.hotpotato.command.base.BaseSubCommand;
import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;

public class SetGameTime extends BaseSubCommand {

    public SetGameTime(String name) {
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
        if (args.length == 2) {
            if (args[1].matches("[0-9]*")) {
                if (Integer.parseInt(args[1]) > 5) {
                    Player player = (Player) sender;
                    hotPotato.roomSetGameTime(Integer.valueOf(args[1]), hotPotato.getRoomConfig(player.getLevel()));
                    sender.sendMessage("§a爆炸等待时间已设置为：" + Integer.valueOf(args[1]));
                } else {
                    sender.sendMessage("§a爆炸等待时间最小不能低于5秒！");
                }
            }else {
                sender.sendMessage("§a时间只能设置为正整数！");
            }
        }else {
            sender.sendMessage("§a查看帮助：/" + getName() + " help");
        }
        return true;
    }

    @Override
    public CommandParameter[] getParameters() {
        return new CommandParameter[] { new CommandParameter("time", CommandParamType.INT, false) };
    }

}
