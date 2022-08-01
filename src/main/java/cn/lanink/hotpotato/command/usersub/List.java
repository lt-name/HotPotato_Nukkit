package cn.lanink.hotpotato.command.usersub;

import cn.lanink.hotpotato.command.base.BaseSubCommand;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParameter;

public class List extends BaseSubCommand {

    public List(String name) {
        super(name);
    }

    @Override
    public boolean canUser(CommandSender sender) {
        return sender.isPlayer();
    }

    @Override
    public String[] getAliases() {
        return new String[0];
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        StringBuilder list = new StringBuilder();
        for (String string : hotPotato.getRooms().keySet()) {
            list.append(string).append(" ");
        }
        sender.sendMessage(this.language.listRoom.replace("%list%", list));
        return true;
    }

    @Override
    public CommandParameter[] getParameters() {
        return new CommandParameter[0];
    }

}
