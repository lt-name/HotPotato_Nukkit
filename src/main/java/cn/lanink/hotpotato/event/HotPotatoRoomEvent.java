package cn.lanink.hotpotato.event;

import cn.lanink.hotpotato.room.Room;
import cn.nukkit.event.Event;

public abstract class HotPotatoRoomEvent extends Event {

    protected Room room;

    public HotPotatoRoomEvent() {

    }

    public Room getRoom() {
        return this.room;
    }

}
