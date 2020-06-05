package cn.lanink.hotpotato.command.base.usersub;

import cn.lanink.hotpotato.command.base.BaseSubCommand;
import cn.lanink.hotpotato.room.Room;
import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParameter;

public class Quit extends BaseSubCommand {

    public Quit(String name) {
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
        Player player = (Player) sender;
        for (Room room : hotPotato.getRooms().values()) {
            if (room.isPlaying(player)) {
                room.quitRoom(player, true);
                sender.sendMessage("§a你已退出房间");
                return true;
            }
        }
        sender.sendMessage("§a你本来就不在游戏房间！");
        return true;
    }

    @Override
    public CommandParameter[] getParameters() {
        return new CommandParameter[0];
    }

}
