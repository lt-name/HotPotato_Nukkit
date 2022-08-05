package cn.lanink.hotpotato.event;

import cn.lanink.hotpotato.room.Room;
import cn.nukkit.Player;
import cn.nukkit.event.HandlerList;

/**
 * @author LT_Name
 */
public class HotPotatoRoomPlayerJoinEvent extends HotPotatoRoomPlayerEvent {

    private static final HandlerList handlers = new HandlerList();

    public static HandlerList getHandlers() {
        return handlers;
    }
    
    public HotPotatoRoomPlayerJoinEvent(Room room, Player player) {
        this.room = room;
        this.player = player;
    }
    
}
