package cn.lanink.hotpotato.tasks;

import cn.lanink.hotpotato.HotPotato;
import cn.lanink.hotpotato.event.HotPotatoRoomStartEvent;
import cn.lanink.hotpotato.room.Room;
import cn.lanink.hotpotato.utils.Tools;
import cn.nukkit.level.Sound;
import cn.nukkit.scheduler.PluginTask;

public class WaitTask extends PluginTask<HotPotato> {

    private final Room room;

    public WaitTask(HotPotato owner, Room room) {
        super(owner);
        this.room = room;
    }

    @Override
    public void onRun(int i) {
        if (this.room.getMode() != 1) {
            this.cancel();
        }
        if (this.room.getPlayers().size() >= 3) {
            if (this.room.waitTime > 0) {
                this.room.waitTime--;
                if (this.room.waitTime <= 5) {
                    Tools.addSound(this.room, Sound.RANDOM_CLICK);
                }
            }else {
                owner.getServer().getPluginManager().callEvent(new HotPotatoRoomStartEvent(this.room));
                this.cancel();
            }
        }else if (this.room.getPlayers().size() > 0) {
            if (this.room.waitTime != this.room.getSetWaitTime()) {
                this.room.waitTime = this.room.getSetWaitTime();
            }
        }else {
            this.room.endGame();
            this.cancel();
        }
    }

}
