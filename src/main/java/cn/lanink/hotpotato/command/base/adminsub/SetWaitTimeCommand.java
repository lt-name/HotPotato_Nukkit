package cn.lanink.hotpotato.command.base.adminsub;

import cn.lanink.hotpotato.command.base.BaseSubCommand;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParameter;

public class SetWaitTimeCommand extends BaseSubCommand {

    public SetWaitTimeCommand(String name) {
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
        return false;
    }

    @Override
    public CommandParameter[] getParameters() {
        return new CommandParameter[0];
    }

}
