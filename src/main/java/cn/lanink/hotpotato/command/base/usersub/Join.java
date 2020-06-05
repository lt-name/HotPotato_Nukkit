package cn.lanink.hotpotato.command.base.usersub;

import cn.lanink.hotpotato.command.base.BaseSubCommand;
import cn.lanink.hotpotato.room.Room;
import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;

public class Join extends BaseSubCommand {

    public Join(String name) {
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
        if (hotPotato.getRooms().size() > 0) {
            for (Room room : hotPotato.getRooms().values()) {
                if (room.isPlaying(player)) {
                    sender.sendMessage("§c你已经在一个房间中了!");
                    return true;
                }
            }
            if (player.riding != null) {
                sender.sendMessage("§a请勿在骑乘状态下进入房间！");
                return true;
            }
            if (args.length < 2) {
                for (Room room : hotPotato.getRooms().values()) {
                    if (room.getMode() == 0 || room.getMode() == 1) {
                        room.joinRoom(player);
                        sender.sendMessage("§a已为你随机分配房间！");
                        return true;
                    }
                }
            }else if (hotPotato.getRooms().containsKey(args[1])) {
                Room room = hotPotato.getRooms().get(args[1]);
                if (room.getMode() == 2 || room.getMode() == 3) {
                    sender.sendMessage("§a该房间正在游戏中，请稍后");
                }else if (room.getPlayers().values().size() > 15) {
                    sender.sendMessage("§a该房间已满人，请稍后");
                } else {
                    room.joinRoom(player);
                }
                return true;
            }else {
                sender.sendMessage("§a该房间不存在！");
                return true;
            }
        }
        sender.sendMessage("§a暂无房间可用！");
        return true;
    }

    @Override
    public CommandParameter[] getParameters() {
        return new CommandParameter[] { new CommandParameter("RoomName", CommandParamType.TEXT, false) };
    }

}
