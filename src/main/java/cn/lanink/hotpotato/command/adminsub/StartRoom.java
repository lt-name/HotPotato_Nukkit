package cn.lanink.hotpotato.command.adminsub;

import cn.lanink.hotpotato.command.base.BaseSubCommand;
import cn.lanink.hotpotato.event.HotPotatoRoomStartEvent;
import cn.lanink.hotpotato.room.Room;
import cn.lanink.hotpotato.room.RoomStatus;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParameter;

/**
 * @author lt_name
 */
public class StartRoom extends BaseSubCommand {

    public StartRoom(String name) {
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
        Room room = this.hotPotato.getRooms().get(player.getLevel().getName());
        if (room != null) {
            if (room.getPlayers().size() >= room.getMinPlayers()) {
                if (room.getStatus() == RoomStatus.WAIT_PLAYER) {
                    Server.getInstance().getPluginManager().callEvent(new HotPotatoRoomStartEvent(room));
                    sender.sendMessage(this.language.adminStartRoom);
                }else {
                    sender.sendMessage(this.language.adminStartRoomIsPlaying);
                }
            }else {
                sender.sendMessage(this.language.adminStartNoPlayer.replace("%1%", room.getMinPlayers() + ""));
            }
        }else {
            sender.sendMessage(this.language.adminLevelNoRoom);
        }
        return true;
    }

    @Override
    public CommandParameter[] getParameters() {
        return new CommandParameter[0];
    }
}
