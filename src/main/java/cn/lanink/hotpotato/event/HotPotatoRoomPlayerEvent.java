package cn.lanink.hotpotato.event;

import cn.lanink.hotpotato.room.Room;
import cn.nukkit.event.player.PlayerEvent;


public abstract class HotPotatoRoomPlayerEvent extends PlayerEvent {

    protected Room room;

    public HotPotatoRoomPlayerEvent() {

    }

    public Room getRoom() {
        return this.room;
    }

}
