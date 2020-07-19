package cn.lanink.hotpotato.command.usersub;

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
            if (player.riding != null) {
                sender.sendMessage(this.language.joinRoomIsRiding);
                return true;
            }
            for (Room room : hotPotato.getRooms().values()) {
                if (room.isPlaying(player)) {
                    sender.sendMessage(this.language.joinRoomIsInRoom);
                    return true;
                }
            }
            if (args.length < 2) {
                for (Room room : hotPotato.getRooms().values()) {
                    if ((room.getMode() == 0 || room.getMode() == 1) && room.getPlayers().size() < 16) {
                        room.joinRoom(player);
                        sender.sendMessage(this.language.joinRandomRoom);
                        return true;
                    }
                }
            }else if (hotPotato.getRooms().containsKey(args[1])) {
                Room room = hotPotato.getRooms().get(args[1]);
                if (room.getMode() == 2 || room.getMode() == 3) {
                    sender.sendMessage(this.language.joinRoomIsPlaying);
                }else if (room.getPlayers().values().size() >= 16) {
                    sender.sendMessage(this.language.joinRoomIsFull);
                } else {
                    room.joinRoom(player);
                }
                return true;
            }else {
                sender.sendMessage(this.language.joinRoomIsNotFound);
                return true;
            }
        }
        sender.sendMessage(this.language.joinRoomNotAvailable);
        return true;
    }

    @Override
    public CommandParameter[] getParameters() {
        return new CommandParameter[] { new CommandParameter("RoomName", CommandParamType.TEXT, false) };
    }

}
