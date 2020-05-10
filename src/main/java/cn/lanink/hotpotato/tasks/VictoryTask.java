package cn.lanink.hotpotato.tasks;

import cn.lanink.hotpotato.HotPotato;
import cn.lanink.hotpotato.room.Room;
import cn.lanink.hotpotato.utils.Tools;
import cn.nukkit.Player;
import cn.nukkit.scheduler.AsyncTask;
import cn.nukkit.scheduler.PluginTask;

import java.util.Map;

public class VictoryTask extends PluginTask<HotPotato> {

    private final Room room;
    private int victoryTime;

    public VictoryTask(HotPotato owner, Room room) {
        super(owner);
        this.room = room;
        this.victoryTime = 10;
    }

    @Override
    public void onRun(int i) {
        if (this.room.getMode() != 3) {
            this.cancel();
        }
        if (this.victoryTime < 1) {
            this.room.endGame();
            this.cancel();
        }else {
            this.victoryTime--;
            owner.getServer().getScheduler().scheduleAsyncTask(HotPotato.getInstance(), new AsyncTask() {
                @Override
                public void onRun() {
                    for (Map.Entry<Player, Integer> entry : room.getPlayers().entrySet()) {
                        if (entry.getValue() != 0) {
                            Tools.spawnFirework(entry.getKey());
                        }
                    }
                }
            });
        }
    }

}
