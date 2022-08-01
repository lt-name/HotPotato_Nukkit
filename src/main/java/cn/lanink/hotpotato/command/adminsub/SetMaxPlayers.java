package cn.lanink.hotpotato.command.adminsub;

import cn.lanink.hotpotato.command.base.BaseSubCommand;
import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.utils.Config;

/**
 * @author LT_Name
 */
public class SetMaxPlayers extends BaseSubCommand {

    public SetMaxPlayers(String name) {
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
                Player player = (Player) sender;
                Config config = this.hotPotato.getRoomConfig(player.getLevel());
                config.set("maxPlayers", Integer.parseInt(args[1]));
                config.save();
                sender.sendMessage("最多玩家数量已设置为" + args[1]);
            }else {
                sender.sendMessage("请输入数字！");
            }
        }else {
            sender.sendMessage(this.language.adminHelp.replace("%cmdName%", this.getName()));
        }
        return true;
    }

    @Override
    public CommandParameter[] getParameters() {
        return new CommandParameter[] { CommandParameter.newType("maxPlayers", CommandParamType.INT) };
    }
}
