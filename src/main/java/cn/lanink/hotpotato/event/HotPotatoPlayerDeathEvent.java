package cn.lanink.hotpotato.event;

import cn.lanink.hotpotato.room.Room;
import cn.nukkit.Player;
import cn.nukkit.event.Cancellable;
import cn.nukkit.event.HandlerList;

public class HotPotatoPlayerDeathEvent extends HotPotatoRoomPlayerEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    public static HandlerList getHandlers() {
        return handlers;
    }

    public HotPotatoPlayerDeathEvent(Room room, Player player) {
        this.room = room;
        this.player = player;
    }

}
