package cn.lanink.hotpotato.command.adminsub;

import cn.lanink.hotpotato.command.base.BaseSubCommand;
import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.utils.Config;

import java.util.List;

public class AddRandomSpawn extends BaseSubCommand {

    public AddRandomSpawn(String name) {
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
        Config config = this.hotPotato.getRoomConfig(player.getLevel());
        String s = player.getFloorX() + ":" + player.getFloorY() + ":" + player.getFloorZ();
        List<String> list = config.getStringList("randomSpawn");
        list.add(s);
        config.set("randomSpawn", list);
        config.save();
        sender.sendMessage(this.language.adminAddRandomSpawn.replace("%number%", list.size() + ""));
        return true;
    }

    @Override
    public CommandParameter[] getParameters() {
        return new CommandParameter[0];
    }

}
