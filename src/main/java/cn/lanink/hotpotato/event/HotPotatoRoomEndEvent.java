package cn.lanink.hotpotato.event;

import cn.lanink.hotpotato.room.Room;
import cn.nukkit.Player;
import cn.nukkit.event.Cancellable;
import cn.nukkit.event.HandlerList;

public class HotPotatoRoomEndEvent extends HotPotatoRoomPlayerEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    public static HandlerList getHandlers() {
        return handlers;
    }

    public HotPotatoRoomEndEvent(Room room, Player victoryPlayer) {
        this.room = room;
        this.player = victoryPlayer;
    }

}
