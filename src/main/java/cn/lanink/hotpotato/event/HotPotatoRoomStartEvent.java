package cn.lanink.hotpotato.event;

import cn.lanink.hotpotato.room.Room;
import cn.nukkit.event.Cancellable;
import cn.nukkit.event.HandlerList;

public class HotPotatoRoomStartEvent extends HotPotatoRoomEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    public static HandlerList getHandlers() {
        return handlers;
    }

    public HotPotatoRoomStartEvent(Room room) {
        this.room = room;
    }

}
